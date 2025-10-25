package com.lms.service;

import com.lms.dao.*;
import com.lms.model.*;
import com.lms.util.DateUtil;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * LeaveRequestService - Business Logic cho Leave Request
 */
public class LeaveRequestService {
    
    private LeaveRequestDAO leaveRequestDAO;
    private UserDAO userDAO;
    private LeaveReasonDAO reasonDAO;
    
    public LeaveRequestService() {
        this.leaveRequestDAO = new LeaveRequestDAO();
        this.userDAO = new UserDAO();
        this.reasonDAO = new LeaveReasonDAO();
    }
    
    /**
     * Tạo đơn xin nghỉ phép mới
     */
    public LeaveRequest createLeaveRequest(Integer userId, Integer reasonId, 
                                          LocalDate fromDate, LocalDate toDate, 
                                          String customReason) throws Exception {
        
        // Validate
        if (userId == null || reasonId == null || fromDate == null || toDate == null) {
            throw new IllegalArgumentException("Thiếu thông tin bắt buộc");
        }
        
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước hoặc bằng ngày kết thúc");
        }
        
        // Lấy user
        User user = userDAO.findById(userId);
        if (user == null || !user.getIsActive()) {
            throw new IllegalArgumentException("User không tồn tại hoặc không active");
        }
        
        // Lấy reason
        LeaveReason reason = reasonDAO.findById(reasonId);
        if (reason == null || !reason.getIsActive()) {
            throw new IllegalArgumentException("Lý do nghỉ phép không hợp lệ");
        }
        
        // Tính số ngày nghỉ
        int totalDays = DateUtil.calculateWorkingDays(fromDate, toDate);
        
        // Kiểm tra số ngày phép còn lại
        if (reason.getDeductFromLeave()) {
            if (user.getRemainingLeaveDays().compareTo(new BigDecimal(totalDays)) < 0) {
                throw new IllegalArgumentException(
                    "Số ngày phép không đủ. Còn lại: " + user.getRemainingLeaveDays() + 
                    " ngày, yêu cầu: " + totalDays + " ngày"
                );
            }
        }
        
        // Tạo request object
        LeaveRequest request = new LeaveRequest();
        request.setRequestCode(leaveRequestDAO.generateRequestCode());
        request.setUserId(userId);
        request.setReasonId(reasonId);
        request.setFromDate(fromDate);
        request.setToDate(toDate);
        request.setTotalDays(new BigDecimal(totalDays));
        request.setCustomReason(customReason);
        
        // Set status mặc định là INPROGRESS
        RequestStatusDAO statusDAO = new RequestStatusDAO();
        RequestStatus inProgressStatus = statusDAO.findByCode("INPROGRESS");
        if (inProgressStatus == null) {
            throw new IllegalStateException("Status INPROGRESS không tồn tại trong database");
        }
        request.setStatusId(inProgressStatus.getStatusId());
        
        // Lưu vào database
        boolean created = leaveRequestDAO.create(request);
        if (!created) {
            throw new Exception("Không thể tạo đơn xin nghỉ phép");
        }
        
        System.out.println("Created leave request: " + request.getRequestCode());
        return request;
    }
    
    /**
     * Approve đơn
     */
    public LeaveRequest approveRequest(Integer requestId, Integer managerId, String note) throws Exception {
        
        LeaveRequest request = leaveRequestDAO.findById(requestId);
        if (request == null) {
            throw new IllegalArgumentException("Đơn không tồn tại");
        }
        
        // Kiểm tra status
        if (!"INPROGRESS".equals(request.getStatusCode())) {
            throw new IllegalStateException("Đơn đã được xử lý rồi");
        }
        
        // Lấy user để kiểm tra manager
        User user = userDAO.findById(request.getUserId());
        User manager = userDAO.findById(managerId);
        
        if (manager == null) {
            throw new IllegalArgumentException("Manager không tồn tại");
        }
        
        if (user.getManagerId() == null || !user.getManagerId().equals(managerId)) {
            throw new IllegalArgumentException("Bạn không có quyền duyệt đơn này");
        }
        
        // Get APPROVED status
        RequestStatusDAO statusDAO = new RequestStatusDAO();
        RequestStatus approvedStatus = statusDAO.findByCode("APPROVED");
        if (approvedStatus == null) {
            throw new IllegalStateException("Status APPROVED không tồn tại");
        }
        
        // Update status
        boolean updated = leaveRequestDAO.updateStatus(
            requestId, 
            approvedStatus.getStatusId(), 
            managerId, 
            note
        );
        
        if (!updated) {
            throw new Exception("Không thể cập nhật trạng thái đơn");
        }
        
        // Trừ số ngày phép
        LeaveReason reason = reasonDAO.findById(request.getReasonId());
        if (reason != null && reason.getDeductFromLeave()) {
            BigDecimal newRemainingDays = user.getRemainingLeaveDays().subtract(request.getTotalDays());
            userDAO.updateRemainingLeaveDays(user.getUserId(), newRemainingDays);
        }
        
        System.out.println("Approved request: " + request.getRequestCode());
        return leaveRequestDAO.findById(requestId);
    }
    
    /**
     * Reject đơn
     */
    public LeaveRequest rejectRequest(Integer requestId, Integer managerId, String note) throws Exception {
        
        LeaveRequest request = leaveRequestDAO.findById(requestId);
        if (request == null) {
            throw new IllegalArgumentException("Đơn không tồn tại");
        }
        
        // Kiểm tra status
        if (!"INPROGRESS".equals(request.getStatusCode())) {
            throw new IllegalStateException("Đơn đã được xử lý rồi");
        }
        
        // Lấy user để kiểm tra manager
        User user = userDAO.findById(request.getUserId());
        User manager = userDAO.findById(managerId);
        
        if (manager == null) {
            throw new IllegalArgumentException("Manager không tồn tại");
        }
        
        if (user.getManagerId() == null || !user.getManagerId().equals(managerId)) {
            throw new IllegalArgumentException("Bạn không có quyền từ chối đơn này");
        }
        
        // Get REJECTED status
        RequestStatusDAO statusDAO = new RequestStatusDAO();
        RequestStatus rejectedStatus = statusDAO.findByCode("REJECTED");
        if (rejectedStatus == null) {
            throw new IllegalStateException("Status REJECTED không tồn tại");
        }
        
        // Update status
        boolean updated = leaveRequestDAO.updateStatus(
            requestId, 
            rejectedStatus.getStatusId(), 
            managerId, 
            note
        );
        
        if (!updated) {
            throw new Exception("Không thể cập nhật trạng thái đơn");
        }
        
        System.out.println("Rejected request: " + request.getRequestCode());
        return leaveRequestDAO.findById(requestId);
    }
    
    /**
     * Lấy đơn của user
     */
    public List<LeaveRequest> getUserRequests(Integer userId) {
        return leaveRequestDAO.findByUserId(userId);
    }
    
    /**
     * Lấy đơn cần manager xét duyệt
     */
    public List<LeaveRequest> getPendingRequestsForManager(Integer managerId) {
        return leaveRequestDAO.findPendingByManagerId(managerId);
    }
    
    /**
     * Lấy đơn của subordinates
     */
    public List<LeaveRequest> getSubordinatesRequests(Integer managerId) {
        List<User> subordinates = userDAO.findSubordinates(managerId);
        List<Integer> subordinateIds = subordinates.stream()
            .map(User::getUserId)
            .collect(Collectors.toList());
        
        return leaveRequestDAO.findBySubordinates(subordinateIds);
    }
    
    /**
     * Lấy agenda của division
     */
    public List<LeaveRequest> getDivisionAgenda(Integer divisionId, LocalDate startDate, LocalDate endDate) {
        return leaveRequestDAO.findByDivisionAndDateRange(divisionId, startDate, endDate);
    }
}