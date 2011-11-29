/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsquare;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author sandro
 */
public class login extends HttpServlet {

    String clid = "xxx";
    String clsecret = "xxx";
    String redirect_uri = "http://201.43.31.165:8080/foursquare/auth";
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        session.setAttribute("clkey", clid);  
        session.setAttribute("clsecret", clsecret);  
        session.setAttribute("redirect_uri", redirect_uri);
        
        PrintWriter out = response.getWriter();
        try {
            String url = "https://foursquare.com/oauth2/authenticate?client_id=" + clid + "&response_type=code&redirect_uri=" + redirect_uri;
            response.sendRedirect(url);
            System.out.println("sendredirect pass");
        } finally {            
            out.close();
        }
    }

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
