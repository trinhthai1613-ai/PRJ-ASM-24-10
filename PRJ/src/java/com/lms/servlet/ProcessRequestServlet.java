package com.lms.servlet;

import com.lms.dao.LeaveRequestDAO;
import com.lms.model.User;
import com.lms.model.LeaveRequest;
import com.lms.service.LeaveRequestService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/request/process")
public class ProcessRequestServlet extends HttpServlet {
    
    private LeaveRequestService leaveRequestService;
    private LeaveRequestDAO leaveRequestDAO;
    
    @Override
    public void init() throws ServletException {
        this.leaveRequestService = new LeaveRequestService();
        this.leaveRequestDAO = new LeaveRequestDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String requestIdStr = request.getParameter("id");
        if (requestIdStr == null) {
            response.sendRedirect(request.getContextPath() + "/request/pending");
            return;
        }
        
        try {
            Integer requestId = Integer.parseInt(requestIdStr);
            LeaveRequest leaveRequest = leaveRequestDAO.findById(requestId);
            
            if (leaveRequest == null) {
                response.sendRedirect(request.getContextPath() + "/request/pending");
                return;
            }
            
            // Set attributes
            request.setAttribute("leaveRequest", leaveRequest);
            
            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/views/request/process.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/request/pending");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User manager = (User) session.getAttribute("user");
        
        try {
            Integer requestId = Integer.parseInt(request.getParameter("requestId"));
            String action = request.getParameter("action");
            String note = request.getParameter("note");
            
            LeaveRequest result;
            
            if ("approve".equals(action)) {
                result = leaveRequestService.approveRequest(requestId, manager.getUserId(), note);
                session.setAttribute("successMessage", "Đã duyệt đơn: " + result.getRequestCode());
            } else if ("reject".equals(action)) {
                result = leaveRequestService.rejectRequest(requestId, manager.getUserId(), note);
                session.setAttribute("successMessage", "Đã từ chối đơn: " + result.getRequestCode());
            }
            
            response.sendRedirect(request.getContextPath() + "/request/pending");
            
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            doGet(request, response);
        }
    }
}