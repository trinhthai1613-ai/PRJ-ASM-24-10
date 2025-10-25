package com.lms.servlet;

import com.lms.model.User;
import com.lms.service.AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * LoginServlet - Xử lý login
 * URL: /login
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    private AuthenticationService authService;
    
    @Override
    public void init() throws ServletException {
        this.authService = new AuthenticationService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Nếu đã login rồi thì redirect về dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        
        // Forward sang JSP (nếu có JSP)
        // request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        
        // Tạm thời hiển thị HTML form đơn giản
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(getLoginFormHTML(request));
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try {
            User user = authService.authenticate(username, password);
            
            if (user != null) {
                // Tạo session
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("fullName", user.getFullName());
                session.setAttribute("divisionId", user.getDivisionId());
                session.setAttribute("managerId", user.getManagerId());
                session.setMaxInactiveInterval(30 * 60); // 30 minutes
                
                // Redirect về dashboard
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                // Login failed - set error message
                request.setAttribute("errorMessage", "Username hoặc password không đúng");
                request.setAttribute("username", username);
                
                // Forward sang JSP (nếu có)
                // request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                
                // Tạm thời hiển thị error
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().write(getLoginFormHTML(request));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(getLoginFormHTML(request));
        }
    }
    
    /**
     * HTML form tạm thời (sẽ thay bằng JSP sau)
     */
    private String getLoginFormHTML(HttpServletRequest request) {
        String errorMessage = (String) request.getAttribute("errorMessage");
        String username = (String) request.getAttribute("username");
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<title>Login - Leave Management System</title>");
        html.append("</head><body>");
        html.append("<h2>Đăng Nhập</h2>");
        
        if (errorMessage != null) {
            html.append("<p style='color:red;'>").append(errorMessage).append("</p>");
        }
        
        html.append("<form method='post' action='login'>");
        html.append("<table>");
        html.append("<tr><td>Username:</td><td><input type='text' name='username' value='")
            .append(username != null ? username : "").append("' required></td></tr>");
        html.append("<tr><td>Password:</td><td><input type='password' name='password' required></td></tr>");
        html.append("<tr><td colspan='2'><input type='submit' value='Đăng nhập'></td></tr>");
        html.append("</table>");
        html.append("</form>");
        html.append("<hr>");
        html.append("<p><small>Tài khoản mẫu: admin / Password123!</small></p>");
        html.append("</body></html>");
        
        return html.toString();
    }
}