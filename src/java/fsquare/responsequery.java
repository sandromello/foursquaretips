/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsquare;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sandro
 */
public class responsequery extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=ISO-8859-1");
        
        TipsStorage ts = new TipsStorage();
        String[] params = request.getParameterValues("box");
        ArrayList<String> pms = new ArrayList<String>();
        ArrayList<String> venuesText = new ArrayList<String>();
        pms.addAll(Arrays.asList(params));
        ArrayList<String>[] objArrays = new ArrayList[pms.size()];
        for (int loop = 0; loop < pms.size(); loop++){
            String query = String.format("select text from tips, venues where venues.id = tips.venueid and venues.name = '%s'", pms.get(loop));
            //System.out.println(ts.getQuery(query, "text"));
            objArrays[loop] = new ArrayList<String>();
            String[] textVenues = ts.getQuery(query, "text").split("\n");
            objArrays[loop].addAll(Arrays.asList(textVenues));
        }
        
        PrintWriter out = response.getWriter();
        try {    
           out.println("<html><head><title>Resultado</title></head>");
           out.println("<body>");            
           for (int loop=0; loop < pms.size(); loop++){
               out.println("<h3>"+pms.get(loop)+"</h3>");
               //System.out.println(objArrays[loop].size());
               for (int loop2=0; loop2 < objArrays[loop].size(); loop2++){
                   out.println(objArrays[loop].get(loop2)+"<br>");
                   
               }
               
           }
           
        } finally {
            out.println("<br><a href='admin.jsp'>Voltar</a>");
            out.println("</body>");
            out.println("</html>");
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
        PrintWriter out = response.getWriter();
        if (request.getParameterValues("box") == null){
            out.println("<html><head><title>Erro</title></head>");
            out.println("<body>");
            out.println("Parametros incompletos!<br>");
            out.println("<br><a href='admin.jsp'>Voltar</a>");
            out.println("</body>");
            out.println("</html>");
        }else{
            processRequest(request, response);
        }
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
