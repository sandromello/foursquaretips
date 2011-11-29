<%-- 
    Document   : response
    Created on : Nov 20, 2011, 4:25:34 AM
    Author     : sandro
--%>

<%@page import="fsquare.TipsStorage"%>
<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>JSP Page</title>
    </head>
    <body>
        <%
        TipsStorage ts = new TipsStorage();
%>
        <table>
            <tr>
                <td>
                    <h1>PESQUISE PELO NOME DO ESTABELECIMENTO</h1>
                    <form id="target" action="tipvenues">
                        <p>
                            VENUE: <input id="inp01" name="venue" size="30" style="border:1px solid #6d84b4;">
                            COLUNA*: <input id="inp02" name="venue" size="30" style="border:1px solid #6d84b4;">
                            <input type="submit"><br>
                        </p>
                    </form>
                </td>
            </tr>
            <tr>
                <td><div id="response"></div></td>
            </tr>
        </table>
    </body>
</html>
