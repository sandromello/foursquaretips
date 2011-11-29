/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author sandro
 */
public class HttpConnection {
    private String errorIOResponse;
    private String param01 = "1ASQLP41UJK0WUOM0RNEXMJO4SVLVNX3DHQ1FLOLNUF2DJ1K";
    private String param02 = "code";
    private String param03 = "http://localhost:8080/foursquare/auth";
    
    
    public String getUrl(String httpurl){
        try {  
             String sURL = httpurl;
             URL url = new URL(sURL);

             URLConnection httpc = url.openConnection();
             httpc.setDoInput(true);
             httpc.connect();
             BufferedReader in = new BufferedReader(new InputStreamReader(httpc.getInputStream()));  
             String strLine = "";
             String content="";
             while ((strLine = in.readLine()) != null){
                  content=content+strLine;
             }
             return content;

         } catch (Exception e) {
             return ("Exception: " + e.getMessage());
         }
    }
    
    public String requestUrlHttps(String httpsurl){
        try {  
             String sURL = httpsurl;
             URL url = new URL(sURL);
             HttpsURLConnection httpsc = (HttpsURLConnection) url.openConnection();
             httpsc.setDoInput(true);          
             
             httpsc.connect();
             BufferedReader in = new BufferedReader(new InputStreamReader(httpsc.getInputStream(), "utf-8"));  
             String strLine = "";
             String code="";
             while ((strLine = in.readLine()) != null){
                  code=code+strLine;     
             }
             in.close();
             httpsc.getResponseCode();
             return code;
             
         } catch (IOException e) {
             errorIOResponse = e.getMessage();
             return "-1";
         }
    }
    
    public String getErrorResponse(){
        return errorIOResponse;
    }
    
}