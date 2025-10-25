package com.lms.dao;

import com.lms.model.LeaveRequest;
import com.lms.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * LeaveRequestDAO - Data Access Object cho LeaveRequest
 */
public class LeaveRequestDAO {
    
    /**
     * Tạo đơn mới
     */
    public boolean create(LeaveRequest request) {
        String sql = "INSERT INTO LeaveRequests (RequestCode, UserID, ReasonID, FromDate, ToDate, " +
                    "TotalDays, CustomReason, StatusID, CreatedAt, UpdatedAt) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, request.getRequestCode());
            stmt.setInt(2, request.getUserId());
            stmt.setInt(3, request.getReasonId());
            stmt.setDate(4, Date.valueOf(request.getFromDate()));
            stmt.setDate(5, Date.valueOf(request.getToDate()));
            stmt.setBigDecimal(6, request.getTotalDays());
            stmt.setString(7, request.getCustomReason());
            stmt.setInt(8, request.getStatusId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    request.setRequestId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Lấy đơn theo ID
     */
    public LeaveRequest findById(Integer requestId) {
        String sql = "SELECT lr.*, " +
                    "u.FullName as UserName, u.EmployeeCode as UserEmployeeCode, " +
                    "r.ReasonName, " +
                    "s.StatusCode, s.StatusName, s.ColorCode as StatusColor, " +
                    "p.FullName as ProcessedByName " +
                    "FROM LeaveRequests lr " +
                    "INNER JOIN Users u ON lr.UserID = u.UserID " +
                    "INNER JOIN LeaveReasons r ON lr.ReasonID = r.ReasonID " +
                    "INNER JOIN RequestStatuses s ON lr.StatusID = s.StatusID " +
                    "LEFT JOIN Users p ON lr.ProcessedBy = p.UserID " +
                    "WHERE lr.RequestID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractLeaveRequestFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy tất cả đơn của user
     */
    public List<LeaveRequest> findByUserId(Integer userId) {
        String sql = "SELECT lr.*, " +
                    "u.FullName as UserName, u.EmployeeCode as UserEmployeeCode, " +
                    "r.ReasonName, " +
                    "s.StatusCode, s.StatusName, s.ColorCode as StatusColor, " +
                    "p.FullName as ProcessedByName " +
                    "FROM LeaveRequests lr " +
                    "INNER JOIN Users u ON lr.UserID = u.UserID " +
                    "INNER JOIN LeaveReasons r ON lr.ReasonID = r.ReasonID " +
                    "INNER JOIN RequestStatuses s ON lr.StatusID = s.StatusID " +
                    "LEFT JOIN Users p ON lr.ProcessedBy = p.UserID " +
                    "WHERE lr.UserID = ? " +
                    "ORDER BY lr.CreatedAt DESC";
        
        return executeQuery(sql, userId);
    }
    
    /**
     * Lấy đơn cần xét duyệt của manager
     */
    public List<LeaveRequest> findPendingByManagerId(Integer managerId) {
        String sql = "SELECT lr.*, " +
                    "u.FullName as UserName, u.EmployeeCode as UserEmployeeCode, " +
                    "r.ReasonName, " +
                    "s.StatusCode, s.StatusName, s.ColorCode as StatusColor, " +
                    "p.FullName as ProcessedByName " +
                    "FROM LeaveRequests lr " +
                    "INNER JOIN Users u ON lr.UserID = u.UserID " +
                    "INNER JOIN LeaveReasons r ON lr.ReasonID = r.ReasonID " +
                    "INNER JOIN RequestStatuses s ON lr.StatusID = s.StatusID " +
                    "LEFT JOIN Users p ON lr.ProcessedBy = p.UserID " +
                    "WHERE u.ManagerID = ? AND s.StatusCode = 'INPROGRESS' " +
                    "ORDER BY lr.CreatedAt ASC";
        
        return executeQuery(sql, managerId);
    }
    
    /**
     * Lấy tất cả đơn của subordinates
     */
    public List<LeaveRequest> findBySubordinates(List<Integer> subordinateIds) {
        if (subordinateIds == null || subordinateIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Build IN clause
        StringBuilder inClause = new StringBuilder();
        for (int i = 0; i < subordinateIds.size(); i++) {
            inClause.append("?");
            if (i < subordinateIds.size() - 1) {
                inClause.append(",");
            }
        }
        
        String sql = "SELECT lr.*, " +
                    "u.FullName as UserName, u.EmployeeCode as UserEmployeeCode, " +
                    "r.ReasonName, " +
                    "s.StatusCode, s.StatusName, s.ColorCode as StatusColor, " +
                    "p.FullName as ProcessedByName " +
                    "FROM LeaveRequests lr " +
                    "INNER JOIN Users u ON lr.UserID = u.UserID " +
                    "INNER JOIN LeaveReasons r ON lr.ReasonID = r.ReasonID " +
                    "INNER JOIN RequestStatuses s ON lr.StatusID = s.StatusID " +
                    "LEFT JOIN Users p ON lr.ProcessedBy = p.UserID " +
                    "WHERE lr.UserID IN (" + inClause + ") " +
                    "ORDER BY lr.CreatedAt DESC";
        
        List<LeaveRequest> requests = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (int i = 0; i < subordinateIds.size(); i++) {
                stmt.setInt(i + 1, subordinateIds.get(i));
            }
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                requests.add(extractLeaveRequestFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }
    
    /**
     * Lấy đơn theo division và date range (cho agenda)
     */
    public List<LeaveRequest> findByDivisionAndDateRange(Integer divisionId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT lr.*, " +
                    "u.FullName as UserName, u.EmployeeCode as UserEmployeeCode, " +
                    "r.ReasonName, " +
                    "s.StatusCode, s.StatusName, s.ColorCode as StatusColor, " +
                    "p.FullName as ProcessedByName " +
                    "FROM LeaveRequests lr " +
                    "INNER JOIN Users u ON lr.UserID = u.UserID " +
                    "INNER JOIN LeaveReasons r ON lr.ReasonID = r.ReasonID " +
                    "INNER JOIN RequestStatuses s ON lr.StatusID = s.StatusID " +
                    "LEFT JOIN Users p ON lr.ProcessedBy = p.UserID " +
                    "WHERE u.DivisionID = ? " +
                    "AND lr.FromDate <= ? AND lr.ToDate >= ? " +
                    "AND s.StatusCode IN ('APPROVED', 'INPROGRESS') " +
                    "ORDER BY u.FullName, lr.FromDate";
        
        List<LeaveRequest> requests = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, divisionId);
            stmt.setDate(2, Date.valueOf(endDate));
            stmt.setDate(3, Date.valueOf(startDate));
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                requests.add(extractLeaveRequestFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }
    
    /**
     * Update request status (approve/reject)
     */
    public boolean updateStatus(Integer requestId, Integer statusId, Integer processedBy, String processNote) {
        String sql = "UPDATE LeaveRequests SET " +
                    "StatusID = ?, ProcessedBy = ?, ProcessedAt = GETDATE(), " +
                    "ProcessNote = ?, UpdatedAt = GETDATE() " +
                    "WHERE RequestID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, statusId);
            stmt.setInt(2, processedBy);
            stmt.setString(3, processNote);
            stmt.setInt(4, requestId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Generate request code
     */
    public String generateRequestCode() {
        String sql = "SELECT COUNT(*) FROM LeaveRequests";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                int count = rs.getInt(1);
                int year = LocalDate.now().getYear();
                return "LR" + year + String.format("%06d", count + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "LR" + LocalDate.now().getYear() + "000001";
    }
    
    /**
     * Helper method to execute query
     */
    private List<LeaveRequest> executeQuery(String sql, Integer parameter) {
        List<LeaveRequest> requests = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, parameter);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                requests.add(extractLeaveRequestFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }
    
    /**
     * Extract LeaveRequest from ResultSet
     */
    private LeaveRequest extractLeaveRequestFromResultSet(ResultSet rs) throws SQLException {
        LeaveRequest request = new LeaveRequest();
        request.setRequestId(rs.getInt("RequestID"));
        request.setRequestCode(rs.getString("RequestCode"));
        request.setUserId(rs.getInt("UserID"));
        request.setUserName(rs.getString("UserName"));
        request.setUserEmployeeCode(rs.getString("UserEmployeeCode"));
        request.setReasonId(rs.getInt("ReasonID"));
        request.setReasonName(rs.getString("ReasonName"));
        
        Date fromDate = rs.getDate("FromDate");
        if (fromDate != null) {
            request.setFromDate(fromDate.toLocalDate());
        }
        
        Date toDate = rs.getDate("ToDate");
        if (toDate != null) {
            request.setToDate(toDate.toLocalDate());
        }
        
        request.setTotalDays(rs.getBigDecimal("TotalDays"));
        request.setCustomReason(rs.getString("CustomReason"));
        request.setStatusId(rs.getInt("StatusID"));
        request.setStatusCode(rs.getString("StatusCode"));
        request.setStatusName(rs.getString("StatusName"));
        request.setStatusColor(rs.getString("StatusColor"));
        request.setProcessedBy((Integer) rs.getObject("ProcessedBy"));
        request.setProcessedByName(rs.getString("ProcessedByName"));
        
        Timestamp processedAt = rs.getTimestamp("ProcessedAt");
        if (processedAt != null) {
            request.setProcessedAt(processedAt.toLocalDateTime());
        }
        
        request.setProcessNote(rs.getString("ProcessNote"));
        
        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        if (createdAt != null) {
            request.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("UpdatedAt");
        if (updatedAt != null) {
            request.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return request;
    }
}