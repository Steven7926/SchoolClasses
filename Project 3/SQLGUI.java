/* Name: Steven Hudson
 Course: CNT 4714 – Spring 2020
 Assignment title: Project 3 – Two-Tier Client-Server Application Development With MySQL and JDBC
 Date: Sunday February 21, 2020
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import java.sql.*;

public class SQLGUI extends JFrame
{
    String jdbcOption [] = {
        "com.mysql.cj.jdbc.Driver",
        "oracle.jdbc.driver.OracleDriver",
        "COM.ibm.db2.jdbc.net.DB2Driver",
        "com.jdbc.odbc.JdbcOdbcDriver"
    };
    String urls [] = {
        "jdbc:mysql://localhost:3312/project3",
        "jdbc:mysql://localhost:3312/bikedb",
        "jdbc:mysql://localhost:3312/testing"
    };
       
    Connection connection = null;

    public SQLGUI() throws SQLException, ClassNotFoundException
    {
        // Set Title of Window
        super("SQL Client GUI - (MJL - CNT4714 - Spring 2020)");

        // Create Panels for each section of the Window
        JPanel databaseInfo = new JPanel();
        JPanel sqlCommand = new JPanel();
        JPanel buttons = new JPanel();
        JPanel resultWindow = new JPanel();
        JPanel finalBut = new JPanel();

        // Create all necessary Labels
        JLabel enterData = new JLabel("<HTML><U>Enter Database Information</U></HTML>");
        JLabel driver = new JLabel("JDBC Driver: ");
        JLabel dataUrl = new JLabel("Database URL: ");
        JLabel user = new JLabel("Username: ");
        JLabel pass = new JLabel("Password: ");
        JLabel commandWin = new JLabel("<HTML><U>Enter a SQL Command</HTML></U>");
        JLabel execWin = new JLabel("<HTML><U>SQL Execution Window</HTML></U>");
        JLabel statusDB = new JLabel("No Connection");

        // Create all necessary Buttons
        JButton connectDB = new JButton("Connect to Database");
        JButton clearCom = new JButton("Clear SQL Command");
        JButton execCom = new JButton("Execute SQL Command");
        JButton clearResWin = new JButton("Clear Result Window");

        // Create all necessary TextFields, Text Areas, and Drop Downs 
        JComboBox<Object> jdbc = new JComboBox<>(jdbcOption);
        JComboBox<Object> urlDB = new JComboBox<>(urls);
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        JTextArea comArea = new JTextArea(null, 5, 5);
        JTable table = new JTable();
        JScrollPane pane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        									ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Make the layouts for each panel that sections the overall window. 
        FlowLayout infoLay = new FlowLayout(FlowLayout.LEFT);
        FlowLayout commandLay = new FlowLayout(FlowLayout.LEFT);
        FlowLayout buttonLay = new FlowLayout(FlowLayout.LEFT);
        FlowLayout resLay = new FlowLayout(FlowLayout.LEFT);
        FlowLayout lastBut = new FlowLayout(FlowLayout.LEFT);

        // Set the layouts
        databaseInfo.setLayout(infoLay);
        sqlCommand.setLayout(commandLay);
        buttons.setLayout(buttonLay);
        resultWindow.setLayout(resLay);
        finalBut.setLayout(lastBut);

        // Config DB Info Panel
        databaseInfo.setPreferredSize(new Dimension(365, 170));
        databaseInfo.setBackground(Color.DARK_GRAY);
        databaseInfo.setVisible(true);

        // Config SQL Command Window Panel
        sqlCommand.setPreferredSize(new Dimension(365,170));
        sqlCommand.setBackground(Color.DARK_GRAY);
        sqlCommand.setVisible(true);

        // Config Buttons Panel
        buttons.setPreferredSize(new Dimension(735, 38));
        buttons.setBackground(Color.DARK_GRAY);
        buttons.setVisible(true);

        // Config Result Window Pannel
        resultWindow.setPreferredSize(new Dimension(735, 370));
        resultWindow.setBackground(Color.DARK_GRAY);
        resultWindow.setVisible(true);

        // Config Final Button Panel 
        finalBut.setBackground(Color.DARK_GRAY);
        finalBut.setVisible(true);

        // Add Panels
        add(databaseInfo);
        add(sqlCommand);
        add(buttons);
        add(resultWindow);
        add(finalBut);

        // Start Configuring and adding components to their proper panels. 
        // Database Information Panel Configuration
        enterData.setForeground(Color.WHITE);
        enterData.setPreferredSize(new Dimension(351, 15));
        enterData.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        databaseInfo.add(enterData);

        driver.setForeground(Color.WHITE);
        driver.setPreferredSize(new Dimension(90, 25));
        databaseInfo.add(driver);

        jdbc.setPreferredSize(new Dimension(240, 25));
        databaseInfo.add(jdbc);

        dataUrl.setForeground(Color.WHITE);
        dataUrl.setPreferredSize(new Dimension(90, 25));
        databaseInfo.add(dataUrl);

        urlDB.setPreferredSize(new Dimension(240, 25));
        databaseInfo.add(urlDB);

        user.setForeground(Color.WHITE);
        user.setPreferredSize(new Dimension(90, 25));
        databaseInfo.add(user);

        username.setPreferredSize(new Dimension(240, 25));
        databaseInfo.add(username);

        pass.setForeground(Color.WHITE);
        pass.setPreferredSize(new Dimension(90, 25));
        databaseInfo.add(pass);

        password.setPreferredSize(new Dimension(240, 25));
        databaseInfo.add(password);

        // SQL Command Panel Configuration
        commandWin.setForeground(Color.WHITE);
        commandWin.setPreferredSize(new Dimension(351, 15));
        commandWin.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        sqlCommand.add(commandWin);

        comArea.setForeground(Color.BLACK);
        comArea.setPreferredSize(new Dimension (358, 150));
        comArea.setLineWrap(true);
        sqlCommand.add(comArea);

        // Button Panel Configuration
        statusDB.setPreferredSize(new Dimension(235, 35));
        statusDB.setOpaque(true);
        statusDB.setForeground(Color.RED);
        statusDB.setBackground(Color.BLACK);
        buttons.add(statusDB);

        connectDB.setPreferredSize(new Dimension(154, 35));
        connectDB.setOpaque(true);
        connectDB.setForeground(Color.YELLOW);
        connectDB.setBackground(Color.BLUE);
        buttons.add(connectDB);

        clearCom.setPreferredSize(new Dimension(154, 35));
        clearCom.setOpaque(true);
        clearCom.setForeground(Color.RED);
        clearCom.setBackground(Color.WHITE);
        buttons.add(clearCom);

        execCom.setPreferredSize(new Dimension(170, 35));
        execCom.setOpaque(true);
        execCom.setForeground(Color.BLACK);
        execCom.setBackground(Color.GREEN);
        buttons.add(execCom);

        // Execution Window Configuration
        execWin.setForeground(Color.WHITE);
        execWin.setPreferredSize(new Dimension(735, 25));
        execWin.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        resultWindow.add(execWin);
        
        pane.setPreferredSize(new Dimension(732, 338));
        pane.setOpaque(true);
        pane.getViewport().setBackground(Color.GRAY);
        table.setGridColor(Color.DARK_GRAY);
		resultWindow.add(pane);


        // Final Button Panel Configuration
        clearResWin.setPreferredSize(new Dimension(171, 35));
        clearResWin.setOpaque(true);
        clearResWin.setForeground(Color.BLACK);
        clearResWin.setBackground(Color.YELLOW);
        finalBut.add(clearResWin);
        
        Font defFont = statusDB.getFont();
        
        DefaultTableModel model = new DefaultTableModel(0, 0) {
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };

        connectDB.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String driver = jdbc.getSelectedItem().toString();
                String theURL = urlDB.getSelectedItem().toString();
                String userInput = username.getText().toString();
                String passInput = password.getText().toString();
                Connection connect = null;               
                connectToDB(connect, statusDB, driver, theURL, userInput, passInput, defFont);  
            }
        });
        
        clearCom.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	comArea.setText("");
            }
        });
        
        clearResWin.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	model.setRowCount(0);
            	model.setColumnCount(0);
            }
        });
        
        execCom.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)  
            {	
        		if (connection != null && comArea.getText() == "")
        			JOptionPane.showMessageDialog(null, "Please enter a SQL command", "No SQL Command Found", JOptionPane.INFORMATION_MESSAGE);
        		if (connection == null)
        			JOptionPane.showMessageDialog(null, "Please Connect to a database first.", "No Database Found", JOptionPane.INFORMATION_MESSAGE);
        		if (comArea.getText() != "" && connection != null)
            		executeQ(model, table, comArea);
            }
        });
    }
    
    public void connectToDB(Connection connect, JLabel statusDB, String driver, String theURL, String userInput, String passInput, Font defFont)
    {
    	try
        {
            Class.forName(driver);
        }
        catch(ClassNotFoundException c) 
        {
        	c.printStackTrace();
        	JOptionPane.showMessageDialog(null, "The driver " + c.getMessage() + " was not found.", "Whoops!", JOptionPane.ERROR_MESSAGE);
        }
        try 
        {
        	connect = DriverManager.getConnection(theURL, userInput, passInput);
        	connection = connect;
		} 
        catch (SQLException sqlIssue) 
        {
			sqlIssue.printStackTrace();	
			JOptionPane.showMessageDialog(null, sqlIssue.getMessage(), "Whoops!", JOptionPane.ERROR_MESSAGE);
			statusDB.setFont(defFont);
			statusDB.setText("No Connection");
			connect = null;
		}
        if (connect != null)
        {
        	statusDB.setFont(new Font("Arial", Font.PLAIN, 10));
        	statusDB.setText("Connected to: " + theURL);
        }
    }
    
    public void executeQ(DefaultTableModel model, JTable table, JTextArea comArea)
    {
    	Statement state = null;
    	int rowCount = 0;
    	int numCol = 0;
    	int count = 1;
    	int howMany = 0;
    	
    	try 
		{
			state = connection.createStatement();
			if (!state.execute(comArea.getText()))
			{
				 howMany = state.executeUpdate(comArea.getText());
				 JOptionPane.showMessageDialog(null, howMany + " (s)" + " rows affected.", "Data Change", JOptionPane.INFORMATION_MESSAGE);				 
			}
			else
			{
				ResultSet res = state.executeQuery(comArea.getText());
				ResultSetMetaData met = res.getMetaData();
				numCol = met.getColumnCount();
					
				while (res.next())
					rowCount++;
				res.beforeFirst();
				
				model.setNumRows(rowCount);
				model.setColumnCount(0);
				
		      			
				for (int i = 1; i <= numCol; i++)
					model.addColumn(met.getColumnName(i));
				
				table.setModel(model);
				
				while (res.next())
				{
					for (int i = 1; i <= numCol; i++)
						table.setValueAt(res.getObject(i), count - 1, i-1);
					count++;
				}
			}
		} 
		catch (SQLException sqlE) 
		{
			sqlE.printStackTrace();
			JOptionPane.showMessageDialog(null, sqlE.getMessage(), "Invalid SQL Command", JOptionPane.ERROR_MESSAGE);
		} 	
    }
}