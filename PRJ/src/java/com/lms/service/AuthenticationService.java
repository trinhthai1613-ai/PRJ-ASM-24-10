package com.lms.service;

import com.lms.dao.UserDAO;
import com.lms.model.User;
import com.lms.util.PasswordUtil;

/**
 * AuthenticationService - Xử lý authentication
 */
public class AuthenticationService {
    
    private UserDAO userDAO;
    
    public AuthenticationService() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * Authenticate user
     * @return User nếu thành công, null nếu thất bại
     */
    public User authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Tìm user
            User user = userDAO.findByUsername(username.trim());
            
            if (user == null) {
                System.out.println("User not found: " + username);
                return null;
            }
            
            // Kiểm tra active
            if (!user.getIsActive()) {
                System.out.println("User is not active: " + username);
                return null;
            }
            
            // Verify password
            if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
                System.out.println("Password verification failed for user: " + username);
                return null;
            }
            
            // Update last login
            userDAO.updateLastLogin(user.getUserId());
            
            System.out.println("User authenticated successfully: " + username);
            return user;
            
        } catch (Exception e) {
            System.err.println("Error during authentication: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}