package com.lms.dao;

import com.lms.model.LeaveReason;
import com.lms.model.RequestStatus;
import com.lms.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class RequestStatusDAO {
    
    /**
     * Tìm status theo code
     */
    public RequestStatus findByCode(String statusCode) {
        String sql = "SELECT * FROM RequestStatuses WHERE StatusCode = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, statusCode);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                RequestStatus status = new RequestStatus();
                status.setStatusId(rs.getInt("StatusID"));
                status.setStatusCode(rs.getString("StatusCode"));
                status.setStatusName(rs.getString("StatusName"));
                status.setDescription(rs.getString("Description"));
                status.setDisplayOrder(rs.getInt("DisplayOrder"));
                status.setColorCode(rs.getString("ColorCode"));
                return status;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy tất cả statuses
     */
    public List<RequestStatus> findAll() {
        String sql = "SELECT * FROM RequestStatuses ORDER BY DisplayOrder";
        List<RequestStatus> statuses = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                RequestStatus status = new RequestStatus();
                status.setStatusId(rs.getInt("StatusID"));
                status.setStatusCode(rs.getString("StatusCode"));
                status.setStatusName(rs.getString("StatusName"));
                status.setDescription(rs.getString("Description"));
                status.setDisplayOrder(rs.getInt("DisplayOrder"));
                status.setColorCode(rs.getString("ColorCode"));
                statuses.add(status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statuses;
    }
}