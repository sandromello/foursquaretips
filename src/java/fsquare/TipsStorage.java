/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fsquare;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.naming.*;
import javax.sql.DataSource;

/**
 *
 * @author sandro
 */
public final class TipsStorage {
    
    private Sexo sex;
    private Connection conn;
    private Statement st;
    private ResultSet rs;
    private String result = null;
    private int rsupdate;
    private int tipsResponse;
    private String errorResponse = null;
    private String errorQuery = null;
    private ArrayList<String> qresult01 = new ArrayList<String>();
    private ArrayList<String> qresult02 = new ArrayList<String>();
    private String errorCode = "0";
    
    public TipsStorage(){}
    public TipsStorage(String venuefsid, String name, Sexo sexo, String text, String date){
        String query = String.format("select name, id from venues where venuefsid = \"%s\"", venuefsid);
        setQueryByIndex(query);
        query = String.format("INSERT INTO tips (text, venuefsid, venue, venueid, user, gender, date)"
                + " VALUES (\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\")", text, venuefsid, qresult01.get(0), qresult02.get(0), name, sexo, date);
        errorQuery = query;
        tipsResponse = setUpdate(query);
    }
    
    public enum Sexo {M, F, N}    
    public Date getDate(long timeInSecs){ 
        long milisec = timeInSecs*1000;
        return new Date(milisec + get1970());
    }
    public int getResponse(){ return tipsResponse; }
    public Sexo getSexo() { return sex; }
    public void setSexo(Sexo sex) { this.sex = sex; }
    
    public static long get1970() {  
        Calendar ano1970 = Calendar.getInstance();  
        ano1970.set(Calendar.YEAR, 1970);  
        ano1970.set(Calendar.MONTH, 0);  
        ano1970.set(Calendar.DAY_OF_MONTH, 1);  
        ano1970.set(Calendar.HOUR_OF_DAY, 0);  
        ano1970.set(Calendar.MINUTE, 0);  
        ano1970.set(Calendar.SECOND, 0);  
        ano1970.set(Calendar.MILLISECOND, 0);  
        return ano1970.getTimeInMillis();  
    }

    public String getQuery(String query, String coluna){
        StringBuilder sb = new StringBuilder();
        try {
            Context ctx = new InitialContext();
            if (ctx == null){
                throw new Exception("No Context");
            }
            
            DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/foursquare");
            if (ds != null){
                conn = ds.getConnection();
                if (conn != null){
                    st = conn.createStatement();
                    rs = st.executeQuery(query);
                    while(rs.next()){
                        result = sb.append(rs.getString(coluna)).append("\n").toString();
                    }
                    conn.close();
                    st.close();
                    rs.close();
                    if (result == null){
                        return "";
                    }else{
                        return result;
                    }
                }
                return "-2";
            }
            return "-2";
            
        } catch (Exception ex) {
            errorResponse = ex.getMessage();
            errorQuery = query;
            return "-1";
        }
    }
    
    public void setQueryByIndex(String query){        
        StringBuilder sb = new StringBuilder();
        try {
            Context ctx = new InitialContext();
            if (ctx == null){
                throw new Exception("No Context");
            }
            
            DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/foursquare");
            if (ds != null){
                conn = ds.getConnection();
                if (conn != null){
                    st = conn.createStatement();
                    rs = st.executeQuery(query);
                    while(rs.next()){
                        qresult01.add(rs.getString(1));
                        qresult02.add(rs.getString(2));
                    }
                    conn.close();
                    st.close();
                    rs.close();
                }else{
                    errorCode = "-2";
                }
            }else{
                errorCode = "-2";
            }
        } catch (Exception ex) {
            errorResponse = ex.getMessage();
            errorQuery = query;
            errorCode = "-1";
        }
    }
    
    public ArrayList<String> getColumn01(){ return qresult01; }
    public ArrayList<String> getColumn02(){ return qresult02; }
    public String getErrorResponse(){ return errorResponse+"<br>QUERY: "+errorQuery; }
    public String getErrorCode(){ return errorCode; }
    
    public int setUpdate(String query){
        try {
            Context ctx = new InitialContext();
            if (ctx == null){
                throw new Exception("No Context");
            }
            
            DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/foursquare");
            if (ds != null){
                conn = ds.getConnection();
                if (conn != null){
                    st = conn.createStatement();
                    rsupdate = st.executeUpdate(query);
                    conn.close();
                }else{
                    errorCode = "-2";
                    return -2;
                }
            }else{
                errorCode = "-2";
                return -2;
            }
            return rsupdate;
        } catch (Exception ex) {
            errorResponse = ex.getMessage();
            errorQuery = query;
            errorCode = "-1";
            return -1;
        }
    }
}
