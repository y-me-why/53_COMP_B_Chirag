package com.demo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/login") 
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username"); 
        String password = request.getParameter("password");

        try {
            Connection con = DBconnection.getConnection(); 
            // Fixed syntax from PDF artifacts to valid SQL
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?"; 
            
            PreparedStatement ps = con.prepareStatement(sql); 
            ps.setString(1, username); 
            ps.setString(2, password); 

            ResultSet rs = ps.executeQuery(); 
            
            if (rs.next()) { 
                // Valid Login
                HttpSession session = request.getSession();
                session.setAttribute("username", username); 
                RequestDispatcher rd = request.getRequestDispatcher("welcome.jsp"); 
                rd.forward(request, response);
            } else { 
                // Invalid Login
                request.setAttribute("error", "Invalid Credentials!"); 
                RequestDispatcher rd = request.getRequestDispatcher("login.jsp"); 
                rd.forward(request, response);
            }
            con.close(); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}