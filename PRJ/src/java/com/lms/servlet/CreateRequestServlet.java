package com.lms.servlet;

import com.lms.dao.LeaveReasonDAO;
import com.lms.model.*;
import com.lms.service.LeaveRequestService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/request/create")
public class CreateRequestServlet extends HttpServlet {
    
    private LeaveRequestService leaveRequestService;
    private LeaveReasonDAO reasonDAO;
    
    @Override
    public void init() throws ServletException {
        this.leaveRequestService = new LeaveRequestService();
        this.reasonDAO = new LeaveReasonDAO();
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
        List<LeaveReason> reasons = reasonDAO.findAllActive();
        
        request.setAttribute("user", user);
        request.setAttribute("reasons", reasons);
        
        // TODO: Forward to JSP
        // request.getRequestDispatcher("/WEB-INF/views/request/create.jsp").forward(request, response);
        
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("CREATE REQUEST PAGE - JSP sẽ được tạo sau");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        try {
            Integer reasonId = Integer.parseInt(request.getParameter("reasonId"));
            LocalDate fromDate = LocalDate.parse(request.getParameter("fromDate"));
            LocalDate toDate = LocalDate.parse(request.getParameter("toDate"));
            String customReason = request.getParameter("customReason");
            
            LeaveRequest leaveRequest = leaveRequestService.createLeaveRequest(
                user.getUserId(), reasonId, fromDate, toDate, customReason
            );
            
            request.setAttribute("successMessage", "Tạo đơn thành công! Mã đơn: " + leaveRequest.getRequestCode());
            request.setAttribute("leaveRequest", leaveRequest);
            
            // TODO: Forward to success page JSP
            response.sendRedirect(request.getContextPath() + "/request/list");
            
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            doGet(request, response);
        }
    }
}