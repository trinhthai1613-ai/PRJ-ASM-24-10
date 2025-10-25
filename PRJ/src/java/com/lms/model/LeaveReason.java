package com.lms.model;

/**
 * LeaveReason Model - Lý do nghỉ phép
 */
public class LeaveReason {
    private Integer reasonId;
    private String reasonCode;
    private String reasonName;
    private String description;
    private Boolean deductFromLeave;
    private Boolean requiresApproval;
    private Integer displayOrder;
    private Boolean isActive;
    
    // Constructors
    public LeaveReason() {
    }
    
    public LeaveReason(Integer reasonId, String reasonCode, String reasonName) {
        this.reasonId = reasonId;
        this.reasonCode = reasonCode;
        this.reasonName = reasonName;
    }
    
    // Getters and Setters
    public Integer getReasonId() {
        return reasonId;
    }
    
    public void setReasonId(Integer reasonId) {
        this.reasonId = reasonId;
    }
    
    public String getReasonCode() {
        return reasonCode;
    }
    
    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }
    
    public String getReasonName() {
        return reasonName;
    }
    
    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getDeductFromLeave() {
        return deductFromLeave;
    }
    
    public void setDeductFromLeave(Boolean deductFromLeave) {
        this.deductFromLeave = deductFromLeave;
    }
    
    public Boolean getRequiresApproval() {
        return requiresApproval;
    }
    
    public void setRequiresApproval(Boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }
    
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    @Override
    public String toString() {
        return "LeaveReason{" +
                "reasonId=" + reasonId +
                ", reasonCode='" + reasonCode + '\'' +
                ", reasonName='" + reasonName + '\'' +
                '}';
    }
}