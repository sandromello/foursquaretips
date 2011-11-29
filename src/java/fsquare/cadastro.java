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

/**
 *
 * @author sandro
 */
public class cadastro extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        StringBuilder sb = new StringBuilder();
        String venuefsid = request.getParameter("venuefsid");
        String venue = request.getParameter("venue");
        int errorIndex = 0;
        int successIndex = 0;
        
        try{
            if(venuefsid == null || venue == null){
                throw new Exception("Parametros incompletos, realize a inclusão <a href='admin.jsp'>novamente!</a><br>");
            }
            
            String query = String.format("INSERT INTO venues (name, venuefsid) VALUES (\"%s\", \"%s\")", venue, venuefsid);
            TipsStorage ts = new TipsStorage();
            int resp = ts.setUpdate(query);
            if (resp != -1 && resp != -2){
                sb.append("Inclusão ocorreu com sucesso!<br>");
                sb.append("<a href='tipsvenue'>MAKE REQUEST</a><br>");
                sb.append("<a href='admin.jsp'>Incluir mais venues</a><br>");
                successIndex++;
            }else{
                sb.append("Erro ao gravar dados: ").append(resp).append(": ").append(ts.getErrorResponse()).append("<br>");
                sb.append("<a href='admin.jsp'>Realizar nova tentativa</a><br>");
                errorIndex++;
            }
        
        }catch (Exception ex){
            sb.append(ex.getMessage());
            errorIndex++;
        }
        
        response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        try {
            out.println("<html><head><title>Administrator</title></head>");
            out.println("<body>");
            out.println("<h3>"+successIndex+" : Dado(s) gravado(s) com sucesso!</h3>");
            out.println("<h3 style='color:red'>"+errorIndex+" : Erro(s) identificado(s)</h3>");
            out.println(sb.toString());
            out.println("</body>");
            out.println("</html>");
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
