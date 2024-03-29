
<!DOCTYPE html>

<%
    String queryBox;
    String results;
    
    queryBox = (String)(session.getAttribute("queryBox"));
    results = (String)(session.getAttribute("results"));

    if (queryBox == null)
    {
        queryBox = "";
    }
    if (results == null)
    {
        results = "";
    }
%>

<html lang="en">
    <style>
        h1
        {
            font-size: xx-large;
        }
        h2
        {
            font-size: x-large;
        }
        hr
        {
            color: beige;
        }
        .heading
        {
            color:beige;
            font-weight: bold;
            font-family: 'Courier New', Courier, monospace;
            text-align: center;
        }
        .description
        {
            color:beige;
            font-weight: bolder;
            font-size: larger;
            font-family: 'Courier New', Courier, monospace;
            text-align: center;
        }
        .textbox
        {
            text-align: left;
            background-color: burlywood;
            color:rgb(0, 82, 235);
            font-family:'Courier New', Courier, monospace;
            font-size: medium;
            font-weight: bold;
            border-radius: 10px;
            width: 40%;
            height: 250px;
        }
        .buttons
        {
            text-align: center;
            font-family:'Courier New', Courier, monospace;
            font-weight: bold;
            background-color: burlywood;
            color:rgb(0, 82, 235);
            border-radius: 5px
            
        }
        .errorA
        {
            text-align: center;
            margin-left:auto; 
            margin-right:auto;
            border: 3px solid white;
            border-radius: 10px;
            width: 40%;
            background-color: rgb(0, 82, 235);
            
        }
        .errorB
        {
            color:beige;
            font-weight: bolder;
            font-family: 'Courier New', Courier, monospace;        
        }
        .errorC
        {
            color:beige;
            font-family: 'Courier New', Courier, monospace;
            width: 100%;
        }
        table.table
        {
            margin-left:auto; 
            margin-right:auto;
            border: 3px solid white;
            border-radius: 10px;
            border-collapse: separate;
            background-color: rgb(0, 82, 235);
        }
        th
        {
            color:beige;
            font-weight: bold;
            font-family: 'Courier New', Courier, monospace;
        }
        tr
        {
            color:beige;
            font-weight: bold;
            font-family: 'Courier New', Courier, monospace;
            text-align: left;
        }
        td
        {
            width: 20%;
        }
    </style>

    <head>
        <title>
            CNT 4714 Remote Database Management System
        </title>
    </head>
    <body style = "background-color: rgb(3, 133, 3)">
        <div class = "mainContent">
            <div class = "heading">
                    <h1>
                        Welcome to the Spring 2020 Project 4 Enterprise Systems
                    </h1>
                    <h2>
                        A remote Database Management System
                    </h2>
            </div>
            <hr>
            <div class = "description">
                    <p>
                        You are connected to the Project 4 Enterprise System database. 
                        </br>
                        Please enter any valid SQL query or update statement.
                        </br>
                        If no query/update command is initially provided, the "Execute Command" button will 
                        display all supplier information in the database.
                        </br>
                        All execution results will appear below. 
                    </p>
            </div>
            <div class = "formspot">
                    <form action = "/project4/sqlquery"  method = "post">
                        <div style = "text-align: center;">
                            <textarea id = "queryArea" class = "textbox" name = "queryBox" rows = "10" cols = "50"><%=queryBox%></textarea>
                        </div>
                        </br>
                        <div style = "text-align: center;">            
                            <input id = "execute" class = "buttons" type = "submit"  value = "Execute Command">                                                 
                    </form>                   
                            <input id = "reset" class = "buttons" type = "submit" onclick = "resetForm();" value = "Reset Form">
                        </div>
            </div>
            <hr>
            <div class = "description">
                <p>
                    Database Results:
                </p>
            </div>
            <div style="text-align: center;">
                <%= results %>
            </div>

        </div>
    </body>

<script>
    function resetForm(){
        document.getElementById('queryArea').value = "";
    }
</script>

</html>

