package com.lms.dao;

import com.lms.model.LeaveReason;
import com.lms.model.RequestStatus;
import com.lms.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * LeaveReasonDAO - DAO cho LeaveReason
 */
public class LeaveReasonDAO {
    
    /**
     * Lấy tất cả lý do nghỉ phép active
     */
    public List<LeaveReason> findAllActive() {
        String sql = "SELECT * FROM LeaveReasons WHERE IsActive = 1 ORDER BY DisplayOrder, ReasonName";
        List<LeaveReason> reasons = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                LeaveReason reason = new LeaveReason();
                reason.setReasonId(rs.getInt("ReasonID"));
                reason.setReasonCode(rs.getString("ReasonCode"));
                reason.setReasonName(rs.getString("ReasonName"));
                reason.setDescription(rs.getString("Description"));
                reason.setDeductFromLeave(rs.getBoolean("DeductFromLeave"));
                reason.setRequiresApproval(rs.getBoolean("RequiresApproval"));
                reason.setDisplayOrder(rs.getInt("DisplayOrder"));
                reason.setIsActive(rs.getBoolean("IsActive"));
                reasons.add(reason);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reasons;
    }
    
    /**
     * Tìm reason theo ID
     */
    public LeaveReason findById(Integer reasonId) {
        String sql = "SELECT * FROM LeaveReasons WHERE ReasonID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reasonId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                LeaveReason reason = new LeaveReason();
                reason.setReasonId(rs.getInt("ReasonID"));
                reason.setReasonCode(rs.getString("ReasonCode"));
                reason.setReasonName(rs.getString("ReasonName"));
                reason.setDescription(rs.getString("Description"));
                reason.setDeductFromLeave(rs.getBoolean("DeductFromLeave"));
                reason.setRequiresApproval(rs.getBoolean("RequiresApproval"));
                reason.setDisplayOrder(rs.getInt("DisplayOrder"));
                reason.setIsActive(rs.getBoolean("IsActive"));
                return reason;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}