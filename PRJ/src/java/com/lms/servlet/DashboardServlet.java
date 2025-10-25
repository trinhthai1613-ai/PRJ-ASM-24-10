package com.lms.servlet;

import com.lms.dao.UserDAO;
import com.lms.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * DashboardServlet - Trang chủ sau khi login
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        this.userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // Refresh user data
        user = userDAO.findById(user.getUserId());
        if (user == null || !user.getIsActive()) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Update session
        session.setAttribute("user", user);
        
        // Set attribute for JSP
        request.setAttribute("user", user);
        
        // Forward sang JSP (nếu có)
        // request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
        
        // Tạm thời hiển thị HTML
        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
    
    private String getDashboardHTML(User user, HttpServletRequest request) {
        String contextPath = request.getContextPath();
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<title>Dashboard</title>");
        html.append("</head><body>");
        html.append("<h1>Dashboard - Xin chào ").append(user.getFullName()).append("!</h1>");
        html.append("<hr>");
        html.append("<table>");
        html.append("<tr><td><strong>Mã NV:</strong></td><td>").append(user.getEmployeeCode()).append("</td></tr>");
        html.append("<tr><td><strong>Email:</strong></td><td>").append(user.getEmail()).append("</td></tr>");
        html.append("<tr><td><strong>Phòng ban:</strong></td><td>").append(user.getDivisionName()).append("</td></tr>");
        html.append("<tr><td><strong>Quản lý:</strong></td><td>")
            .append(user.getManagerName() != null ? user.getManagerName() : "-").append("</td></tr>");
        html.append("<tr><td><strong>Số ngày phép còn lại:</strong></td><td>")
            .append(user.getRemainingLeaveDays()).append(" / ").append(user.getAnnualLeaveDays())
            .append("</td></tr>");
        html.append("</table>");
        html.append("<hr>");
        
        html.append("<h3>Menu:</h3>");
        html.append("<ul>");
        html.append("<li><a href='").append(contextPath).append("/request/create'>Tạo đơn xin nghỉ phép</a></li>");
        html.append("<li><a href='").append(contextPath).append("/request/list'>Xem các đơn đã tạo</a></li>");
        
        if (user.getManagerId() != null) {
            html.append("<li><a href='").append(contextPath).append("/request/subordinates'>Xem đơn của cấp dưới</a></li>");
            html.append("<li><a href='").append(contextPath).append("/request/pending'>Đơn cần xét duyệt</a></li>");
        }
        
        html.append("<li><a href='").append(contextPath).append("/division/agenda'>Xem agenda phòng ban</a></li>");
        html.append("<li><a href='").append(contextPath).append("/logout'>Đăng xuất</a></li>");
        html.append("</ul>");
        html.append("</body></html>");
        
        return html.toString();
    }
}