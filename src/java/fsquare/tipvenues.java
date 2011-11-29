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
import http.HttpConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.jackson.*;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author sandro
 */
public class tipvenues extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        StringBuilder sb = new StringBuilder();
        int metacode = 0;
        int errorIndex = 0;
        int successIndex = 0;
        int venueIndex = 0;
        
        TipsStorage db = new TipsStorage();
        String[] checkdb = db.getQuery("select venuefsid from venues where apirequest = 'false'", "venuefsid").split("\n");
        if (checkdb[0].equals("")){
            sb.append("Não há requisições para processar.<br>");
            sb.append("<a href='admin.jsp'>Cadastrar novas venues.</a><br>");
            sb.append("MAKE UPDATE REQUEST<br>");
            errorIndex++;
        }else if (checkdb[0].equals("-2")){
            sb.append("CheckDB result -1");
            errorIndex++;
        }else if(checkdb[0].equals("-1")){
            sb.append("<h1 style='color: red'>Erro SQL:</h1>");
            sb.append(db.getErrorResponse());
            errorIndex++;
        }else{
            String oauth2 = db.getQuery("select token_id from consumer where fsuserid = '16208212'", "token_id").replace("\\n", "");
            if (oauth2.equals("-1") && oauth2.equals("-2")){
                sb.append("Erro ao recuperar token!");
                sb.append(db.getErrorResponse());
                errorIndex++;
            }else{
                for (int lp = 0; lp < checkdb.length; lp++){                
                    DateFormat df = new SimpleDateFormat("yyyyMMdd");
                    Date dt = new Date();

                    String urlapi = "https://api.foursquare.com/v2/venues/"+checkdb[lp]+"/tips"+"?oauth_token="+oauth2+"&limit=500"+"&v="+df.format(dt);
                    HttpConnection http = new HttpConnection();
                    String httpResponse = http.requestUrlHttps(urlapi);
                    if (httpResponse.equals("-1")){
                        sb.append(http.getErrorResponse());
                        errorIndex++;
                        break;
                    }

                    ObjectMapper om = new ObjectMapper();
                    JsonFactory jf = new JsonFactory();
                    JsonNode node;
                    JsonParser jp = jf.createJsonParser(httpResponse);

                    node = om.readTree(jp);
                    node = node.path("meta");
                    metacode = node.path("code").asInt();
                    if (metacode != 200){
                        sb.append("Não foi possível fazer a requisição ao VENUEID: ").append(checkdb[lp]).append("<br>");
                        sb.append("METACODE Foursquare: ").append(metacode).append("<br>");
                        break;
                    }

                    jp = jf.createJsonParser(httpResponse);
                    TipsStorage ts = new TipsStorage();                
                    ArrayList<String> fullName = new ArrayList<String>();
                    ArrayList<String> gender = new ArrayList<String>();
                    ArrayList<String> text = new ArrayList<String>();
                    ArrayList<Long> createdAt = new ArrayList<Long>();
                    ArrayList<String> userJson = new ArrayList<String>();
                    ArrayList<JsonNode> nodeList = new ArrayList<JsonNode>();

                    while(jp.nextToken() != null){
                        if (jp.getText().equals("items")){
                            while(jp.nextToken() != null){
                                if (jp.getText().equals("id")){
                                    node = om.readTree(jp);
                                    createdAt.add(node.path("createdAt").asLong());
                                    text.add(removeStringQuotes(node.path("text").toString().replace("\\n", " ").replace("\\", "")));
                                    userJson.add(node.path("user").toString());
                                }
                            }break;
                        }
                    }
                    /** Controi uma árvore Json dos usuários */
                    for (int loop = 0; loop < text.size(); loop++){
                        nodeList.add(om.readTree(userJson.get(loop)));
                    }

                    /** Remove itens específicos da árvore nodeList */
                    for (int loop = 0; loop < nodeList.size(); loop++){
                        node = nodeList.get(loop);
                        fullName.add(removeStringQuotes(node.path("firstName").toString())+" "+removeStringQuotes(node.path("lastName").toString()));
                        gender.add(removeStringQuotes(node.path("gender").toString()));
                    }

                    TipsStorage resp;
                    /** Extrai dados de cada array e grava no banco de dados Mysql */
                    for (int loop = 0; loop < text.size(); loop++){                
                        if (gender.get(loop).equals("female")){
                            ts.setSexo(TipsStorage.Sexo.F);
                        }else if(gender.get(loop).equals("none")) {
                            ts.setSexo(TipsStorage.Sexo.N);
                        }else{
                            ts.setSexo(TipsStorage.Sexo.M);
                        }
                        df = new SimpleDateFormat("yyyy/MM/dd");

                        resp = new TipsStorage(checkdb[lp], fullName.get(loop), ts.getSexo(), text.get(loop), df.format(ts.getDate(createdAt.get(loop))));

                        if (resp.getResponse() != -1 && resp.getResponse() != -2){
                            String qry = String.format("UPDATE venues SET apirequest = 'true' WHERE venuefsid = '%s'", checkdb[lp]);
                            ts.setUpdate(qry);
                            successIndex++;
                            if (ts.getErrorCode().equals("-1") && ts.getErrorCode().equals("-2")){
                                sb.append("Erro ao atualizar apirequest, VENUEID: ").append(checkdb[lp]);
                                errorIndex++;
                                break;
                            }
                        }else{
                            sb.append("Erro na execução da query: ").append(resp.getErrorResponse()).append("<br>");
                            errorIndex++;
                        }
                    }
                    venueIndex++;
                }
            }

            response.setContentType("text/html;charset=ISO-8859-1");
            PrintWriter out = response.getWriter();
            try{
                out.println("<html><head><title>Administrator</title></head>");
                out.println("<body>");
                out.println("<h3>"+venueIndex+" : Requisição(ões) realizada(s)!</h3>");
                out.println("<h3>"+successIndex+" : Dado(s) gravado(s) com sucesso!</h3>");
                out.println("<h3 style='color:red'>"+errorIndex+" : Erro(s) identificado(s)</h3>");
                out.println(sb.toString());
                out.println("</body>");
                out.println("</html>");
            }finally{
                out.close();
            }
        }
    }
    
    public final String removeStringQuotes(String s){
        Pattern p = Pattern.compile("\"");
        Matcher m = p.matcher(s);
        StringBuffer sb = new StringBuffer();
        while (m.find()){
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();   
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
