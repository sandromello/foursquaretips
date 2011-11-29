<%-- 
    Document   : admin
    Created on : Nov 20, 2011, 3:05:36 AM
    Author     : sandro
--%>

<%@page import="fsquare.TipsStorage"%>
<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%
    TipsStorage ts = new TipsStorage();
    StringBuilder sb = new StringBuilder();
    String[] venuesName = ts.getQuery("select name from venues", "name").split("\n");
    for (int loop = 0; loop < venuesName.length; loop++){
        if (loop == (venuesName.length - 1)){
            sb.append("'").append(venuesName[loop]).append("'");
            break;
        }
        sb.append("'").append(venuesName[loop]).append("'").append(",");
    }
%>
<html>
    <head>
        <script type="text/javascript" src="js/jquery.js"></script>
        <script type="text/javascript">
            $(document).ready(function(){
                var venues = [<%out.print(sb.toString());%>];
                for (var loop = 0; loop < venues.length; loop++){
                    if(loop == (venues.length - 1)){
                        $("#ele").append("<input type='checkbox' name='box' value='"+venues[loop]+"' />"+venues[loop]+"<br>");
                        $("#ele").append("<input id='all' type='checkbox' />"+"Selecionar tudo"+"<br>");
                        $("#ele").append("<input id='consultasb' type='submit' value='Pesquisar'>");
                        break;
                    }
                    $("#ele").append("<input type='checkbox' name='box' value='"+venues[loop]+"' />"+venues[loop]+"<br>");
                }
                var inp01null = $("#inp01").val();
                var inp02null = $("#inp02").val();
                $("#target").submit(function(e){
                    if ($("#inp01").val() == inp01null || $("#inp02").val() == inp02null){
                        e.preventDefault();
                        alert("Os dois campos precisam ser preenchidos!");
                    }
                });
                $("#all").click(function(){
                    $("input[name=box]").attr("checked", true);
                });
                $("#consultasb").click(function(e){
                    if ($("input:checked").length == 0){
                        e.preventDefault();
                        alert("É preciso selecionar ao menos uma Venue!");
                    }
                });
                
            });

        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Foursquare Admin</title>
    </head>
    <body>
        <table>
            <tr>
                <td>
                    <h2>Cadastrar VENUES:</h2>
                    <form id="target" action="cadastro">
                        <p>
                            VENUE: <input id="inp01" name="venue" size="30" style="border:1px solid #6d84b4;">
                            VENUE ID: <input id="inp02" name="venuefsid" size="30" style="border:1px solid #6d84b4;">
                            <input value="Cadastrar" type="submit"><br>
                        </p>
                    </form>
                </td>
            </tr>
            <tr>
                <td><h4><a href="tipvenues">MAKE REQUEST</a>*</h4></td>
            </tr>
            <tr>
                <td>
                    <p style="font-size: 11px">
                        * Realiza diversas requisições ao Foursquare com os dados existentes no banco de dados.<br>
                        A requisição é apenas realizada uma vez, existirá outro processo** que irá atualizar o banco.<br>
                        ** Ainda não foi desenvolvido.
                    </p>
                </td>
            </tr>
            <tr>
                <td>
                    <h2>CONSULTA:</h2>
                </td>
            </tr>
            <tr>
                <td>
                    <form id="consulta" action="responsequery">
                        <p id="ele">
                            
                        </p>
                    </form>
                </td>
            </tr>
            <tr>
                <td>
                    <p style="font-size: 11px">
                        Os items descritos acima são as "Venues" já cadastradas no banco de dados.<br>
                        A consulta irá trazer a descrição das "Tips" das venues selecionadas.
                        
                    </p>
                </td>
            </tr>
        </table>
    </body>
</html>
