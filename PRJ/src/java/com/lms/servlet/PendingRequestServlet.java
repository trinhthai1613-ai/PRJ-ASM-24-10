package com.lms.servlet;

import com.lms.model.User;
import com.lms.model.LeaveRequest;
import com.lms.service.LeaveRequestService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/request/pending")
public class PendingRequestServlet extends HttpServlet {
    
    private LeaveRequestService leaveRequestService;
    
    @Override
    public void init() throws ServletException {
        this.leaveRequestService = new LeaveRequestService();
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
        
        // Get pending requests for manager
        List<LeaveRequest> requests = leaveRequestService.getPendingRequestsForManager(user.getUserId());
        
        // Set attributes
        request.setAttribute("requests", requests);
        
        // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/views/request/pending.jsp").forward(request, response);
    }
}