package com.lms.dao;

import com.lms.model.User;
import com.lms.util.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO - Data Access Object cho User
 * Xử lý tất cả các thao tác database liên quan đến User
 */
public class UserDAO {
    
    /**
     * Tìm user theo username (cho login)
     */
    public User findByUsername(String username) {
        String sql = "SELECT u.*, d.DivisionName, m.FullName as ManagerName " +
                    "FROM Users u " +
                    "LEFT JOIN Divisions d ON u.DivisionID = d.DivisionID " +
                    "LEFT JOIN Users m ON u.ManagerID = m.UserID " +
                    "WHERE u.Username = ? AND u.IsActive = 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Tìm user theo ID
     */
    public User findById(Integer userId) {
        String sql = "SELECT u.*, d.DivisionName, m.FullName as ManagerName " +
                    "FROM Users u " +
                    "LEFT JOIN Divisions d ON u.DivisionID = d.DivisionID " +
                    "LEFT JOIN Users m ON u.ManagerID = m.UserID " +
                    "WHERE u.UserID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy danh sách subordinates (cấp dưới) của manager
     */
    public List<User> findSubordinates(Integer managerId) {
        String sql = "SELECT u.*, d.DivisionName, m.FullName as ManagerName " +
                    "FROM Users u " +
                    "LEFT JOIN Divisions d ON u.DivisionID = d.DivisionID " +
                    "LEFT JOIN Users m ON u.ManagerID = m.UserID " +
                    "WHERE u.ManagerID = ? AND u.IsActive = 1 " +
                    "ORDER BY u.FullName";
        
        List<User> subordinates = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, managerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                subordinates.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subordinates;
    }
    
    /**
     * Lấy tất cả users theo division
     */
    public List<User> findByDivision(Integer divisionId) {
        String sql = "SELECT u.*, d.DivisionName, m.FullName as ManagerName " +
                    "FROM Users u " +
                    "LEFT JOIN Divisions d ON u.DivisionID = d.DivisionID " +
                    "LEFT JOIN Users m ON u.ManagerID = m.UserID " +
                    "WHERE u.DivisionID = ? AND u.IsActive = 1 " +
                    "ORDER BY u.FullName";
        
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, divisionId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    /**
     * Update last login time
     */
    public void updateLastLogin(Integer userId) {
        String sql = "UPDATE Users SET LastLogin = GETDATE() WHERE UserID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Update remaining leave days
     */
    public void updateRemainingLeaveDays(Integer userId, BigDecimal newRemainingDays) {
        String sql = "UPDATE Users SET RemainingLeaveDays = ?, UpdatedAt = GETDATE() WHERE UserID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, newRemainingDays);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Extract User object from ResultSet
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("UserID"));
        user.setEmployeeCode(rs.getString("EmployeeCode"));
        user.setUsername(rs.getString("Username"));
        user.setPasswordHash(rs.getString("PasswordHash"));
        user.setEmail(rs.getString("Email"));
        user.setFullName(rs.getString("FullName"));
        user.setPhoneNumber(rs.getString("PhoneNumber"));
        user.setDivisionId(rs.getInt("DivisionID"));
        user.setDivisionName(rs.getString("DivisionName"));
        user.setManagerId((Integer) rs.getObject("ManagerID"));
        user.setManagerName(rs.getString("ManagerName"));
        
        Date hireDate = rs.getDate("HireDate");
        if (hireDate != null) {
            user.setHireDate(hireDate.toLocalDate());
        }
        
        user.setAnnualLeaveDays(rs.getInt("AnnualLeaveDays"));
        user.setRemainingLeaveDays(rs.getBigDecimal("RemainingLeaveDays"));
        user.setIsActive(rs.getBoolean("IsActive"));
        
        Timestamp lastLogin = rs.getTimestamp("LastLogin");
        if (lastLogin != null) {
            user.setLastLogin(lastLogin.toLocalDateTime());
        }
        
        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("UpdatedAt");
        if (updatedAt != null) {
            user.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return user;
    }
}