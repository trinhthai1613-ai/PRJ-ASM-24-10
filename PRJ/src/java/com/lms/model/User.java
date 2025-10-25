package com.lms.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * User Model - Đại diện cho một nhân viên
 */
public class User {
    private Integer userId;
    private String employeeCode;
    private String username;
    private String passwordHash;
    private String email;
    private String fullName;
    private String phoneNumber;
    private Integer divisionId;
    private String divisionName;  // Để hiển thị
    private Integer managerId;
    private String managerName;    // Để hiển thị
    private LocalDate hireDate;
    private Integer annualLeaveDays;
    private BigDecimal remainingLeaveDays;
    private Boolean isActive;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public User() {
    }
    
    public User(Integer userId, String username, String fullName, String email) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
    }
    
    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public String getEmployeeCode() {
        return employeeCode;
    }
    
    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public Integer getDivisionId() {
        return divisionId;
    }
    
    public void setDivisionId(Integer divisionId) {
        this.divisionId = divisionId;
    }
    
    public String getDivisionName() {
        return divisionName;
    }
    
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }
    
    public Integer getManagerId() {
        return managerId;
    }
    
    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }
    
    public String getManagerName() {
        return managerName;
    }
    
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
    
    public LocalDate getHireDate() {
        return hireDate;
    }
    
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
    
    public Integer getAnnualLeaveDays() {
        return annualLeaveDays;
    }
    
    public void setAnnualLeaveDays(Integer annualLeaveDays) {
        this.annualLeaveDays = annualLeaveDays;
    }
    
    public BigDecimal getRemainingLeaveDays() {
        return remainingLeaveDays;
    }
    
    public void setRemainingLeaveDays(BigDecimal remainingLeaveDays) {
        this.remainingLeaveDays = remainingLeaveDays;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
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
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}