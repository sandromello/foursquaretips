/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsquare;

//import com.google.gson.Gson;
import http.HttpConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author sandro
 */
public class auth extends HttpServlet {    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String code = request.getParameter("code");
        HttpConnection conn = new HttpConnection();
        
        HttpSession session = request.getSession(true);  
        String clid=(String)session.getAttribute("clkey");  
        String clsecret=(String)session.getAttribute("clsecret");  
        String redirect_uri=(String)session.getAttribute("redirect_uri");
        
        System.out.println("CODE == NULL");
        
        PrintWriter out = response.getWriter();
        try {
            if (!code.equals("")){
                
                String url = "https://foursquare.com/oauth2/access_token?client_id="+clid+"&client_secret="+clsecret+"&grant_type=authorization_code&redirect_uri="+redirect_uri+"&code="+code;
                String jsoncontent = conn.getUrl(url);
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                Date date = new Date();
            }
        } finally {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
}
