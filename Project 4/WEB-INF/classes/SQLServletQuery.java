/* 
	Name: Steven Hudson     
	Course: CNT 4714 – Spring 2020 – Project Four     
	Assignment title:  A Three-Tier Distributed Web-Based Application      
	Date:  April 10, 2020 
*/ 

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLServletQuery extends HttpServlet 
{
	private Connection connection;
	private Statement statement;
	@Override
	public void init(ServletConfig config) throws ServletException 
	{
		super.init(config);
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3312/project4",
					                                 "root", 
					                                 "passwordhere");
			statement = connection.createStatement();
		}

		catch (Exception e) 
		{
			e.printStackTrace();
			throw new UnavailableException(e.getMessage());
		} 
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException 
	{
		String queryBox = request.getParameter("queryBox");
		String queryBoxLowerCase = queryBox.toLowerCase();
		String result = null;
		
		if (queryBoxLowerCase.contains("select")) // Its a select statement
		{

			try 
			{
				result = doSelectQuery(queryBoxLowerCase);
			}  
			catch (SQLException e) 
			{
				result = "<div class = 'errorA'>" +
							"<div class = 'errorB'>" +
								"Error executing the SQL statement: " +
							 "</div>" +
							"<div class = 'errorC'>" +
								 e.getMessage() + 	
							"</div>" +
						 "</div>";
				e.printStackTrace();
			}
		}
		else if (queryBoxLowerCase.isEmpty()) {}
		else // Some sort of other statement; update, insert, etc.
		{ 
			try 
			{
				result = doUpdateQuery(queryBoxLowerCase, queryBox);
			}
			catch(SQLException e) 
			{
				result = "<div class = 'errorA'>" +
							"<div class = 'errorB'>" +
								"Error executing the SQL statement: " +
							"</div>" +
							"<div class = 'errorC'>" +
								e.getMessage() + 	
							"</div>" +
						"</div>";
				e.printStackTrace();
			}
		}

		HttpSession session = request.getSession();
		session.setAttribute("results", result);
		session.setAttribute("queryBox", queryBox);
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/index.jsp");
		dispatch.forward(request, response);
	}

	

	// Process post request
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException 
	{
		doGet(request, response);
	}

	public String doSelectQuery(String queryBox) throws SQLException 
	{
		String tableOpen = "<table class = 'table'>";
		String tableBod = "<tbody>";
		String tableClose = "</table>";
		String tableCols = "<thead><tr>";
		String results = null;
		
		int numCol = 0;

	
		ResultSet res = statement.executeQuery(queryBox);
		ResultSetMetaData met = res.getMetaData();
		numCol = met.getColumnCount();
				
		for (int i = 1; i <= numCol; i++) 
			tableCols += "<th scope ='col'>" + met.getColumnName(i) + "</th>";

		tableCols += "</tr></thead>";

		while (res.next()) 		// Get rows
		{
			tableBod += "<tr>";
			for (int i = 1; i <= numCol; i++) 
			{		
				if (i == 1)
					tableBod += "<td scope = 'row'>" + res.getString(i) + "</th>";
				else
					tableBod += "<td>" + res.getString(i) + "</th>";
			}
			tableBod += "</tr>";
		}

		tableBod += "</tbody>";
		results = tableOpen + tableCols+ tableBod + tableClose;

		return results;
	}
	
	private String doUpdateQuery(String queryBoxLowerCase, String queryBox) throws SQLException {
		
		String results = null;
		int numOfRowsUp = 0;
		int numOfShipsB = 0;
		int numOfShipsA = 0;
		int numOfRowsAffect = 0;
		
		if ((queryBoxLowerCase.contains("insert") || queryBoxLowerCase.contains("update")) && queryBoxLowerCase.contains("shipments"))
		{
			ResultSet beforeQuantityCheck = statement.executeQuery("select COUNT(*) from shipments where quantity >= 100");
			beforeQuantityCheck.next();
			numOfShipsB = beforeQuantityCheck.getInt(1);		

			
			statement.executeUpdate("create table shipsB4Up like shipments");			// Temporary table
			statement.executeUpdate("insert into shipsB4Up (select * from shipments)");		
			
			numOfRowsUp = statement.executeUpdate(queryBox);

			ResultSet postQCheck = statement.executeQuery("select COUNT(*) from shipments where quantity >= 100");
			postQCheck.next();
			numOfShipsA = postQCheck.getInt(1);
			
			results = "<div class = 'errorA'>" +
					"<div class = 'errorC'> The statement executed succesfully.</div>" + 
					"<div class = 'errorC'>" + numOfRowsUp + " row(s) affected.</div>";

			
			
			if(numOfShipsB < numOfShipsA) // Update suppliers if shipment quantity is greater than 100.
			{
				// Increase suppliers status by 5
				numOfRowsAffect = statement.executeUpdate(
															"update suppliers set status = status + 5 where snum "
															+ "in ( select distinct snum from shipments left join shipsB4Up using"
															+ " (snum, pnum, jnum, quantity) where shipsB4Up.snum is null)"
														  );
				
				results += "<div class = 'errorC'> Business logic detected! - Updating Supplier Status </div>" +
							"<div class = 'errorC'> Business logic updated " + numOfRowsAffect + " supplier(s) status marks. </div>" +
							"</div>";
			}
			
			statement.executeUpdate("drop table shipsB4Up"); // Drop the temporary table created.
		}
		else
		{
			numOfRowsUp = statement.executeUpdate(queryBox);
			results = "<div class = 'errorA'>"+
						"<div class = 'errorB'>" +
					     	"The statement executed succesfully " +
					         numOfRowsUp +
					         " row(s) affected" +
					     "</div>" +
					    "</div>";
		}
		return results;
	}

}
