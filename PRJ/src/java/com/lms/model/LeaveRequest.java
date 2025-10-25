package com.lms.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * LeaveRequest Model - Đại diện cho một đơn xin nghỉ phép
 */
public class LeaveRequest {
    private Integer requestId;
    private String requestCode;
    private Integer userId;
    private String userName;           // Để hiển thị
    private String userEmployeeCode;   // Để hiển thị
    private Integer reasonId;
    private String reasonName;         // Để hiển thị
    private LocalDate fromDate;
    private LocalDate toDate;
    private BigDecimal totalDays;
    private String customReason;
    private Integer statusId;
    private String statusCode;         // Để hiển thị
    private String statusName;         // Để hiển thị
    private String statusColor;        // Để hiển thị
    private Integer processedBy;
    private String processedByName;    // Để hiển thị
    private LocalDateTime processedAt;
    private String processNote;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public LeaveRequest() {
    }
    
    // Getters and Setters
    public Integer getRequestId() {
        return requestId;
    }
    
    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }
    
    public String getRequestCode() {
        return requestCode;
    }
    
    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserEmployeeCode() {
        return userEmployeeCode;
    }
    
    public void setUserEmployeeCode(String userEmployeeCode) {
        this.userEmployeeCode = userEmployeeCode;
    }
    
    public Integer getReasonId() {
        return reasonId;
    }
    
    public void setReasonId(Integer reasonId) {
        this.reasonId = reasonId;
    }
    
    public String getReasonName() {
        return reasonName;
    }
    
    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }
    
    public LocalDate getFromDate() {
        return fromDate;
    }
    
    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }
    
    public LocalDate getToDate() {
        return toDate;
    }
    
    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }
    
    public BigDecimal getTotalDays() {
        return totalDays;
    }
    
    public void setTotalDays(BigDecimal totalDays) {
        this.totalDays = totalDays;
    }
    
    public String getCustomReason() {
        return customReason;
    }
    
    public void setCustomReason(String customReason) {
        this.customReason = customReason;
    }
    
    public Integer getStatusId() {
        return statusId;
    }
    
    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
    
    public String getStatusCode() {
        return statusCode;
    }
    
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
    
    public String getStatusName() {
        return statusName;
    }
    
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    
    public String getStatusColor() {
        return statusColor;
    }
    
    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }
    
    public Integer getProcessedBy() {
        return processedBy;
    }
    
    public void setProcessedBy(Integer processedBy) {
        this.processedBy = processedBy;
    }
    
    public String getProcessedByName() {
        return processedByName;
    }
    
    public void setProcessedByName(String processedByName) {
        this.processedByName = processedByName;
    }
    
    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
    
    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
    
    public String getProcessNote() {
        return processNote;
    }
    
    public void setProcessNote(String processNote) {
        this.processNote = processNote;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "LeaveRequest{" +
                "requestId=" + requestId +
                ", requestCode='" + requestCode + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", statusCode='" + statusCode + '\'' +
                '}';
    }
}