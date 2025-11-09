package ASimulatorSystem;

import java.sql.*;  
import javax.swing.JOptionPane;

public class Conn{
    Connection c;
    Statement s;
    public Conn(){  
        try{  
            // It is recommended to use environment variables for database credentials
            // For example:
            // String dbUser = System.getenv("DB_USER");
            // String dbPassword = System.getenv("DB_PASSWORD");
            // String dbUrl = "jdbc:mysql:///bankmanagementsystem";
            // If you are using a local database, you can set these environment variables in your system
            // For now, we are using placeholders for demonstration purposes
            // Please replace "YOUR_USERNAME" and "YOUR_PASSWORD" with your database credentials
            Class.forName("com.mysql.cj.jdbc.Driver");  
            c =DriverManager.getConnection("jdbc:mysql:///bankmanagementsystem","YOUR_USERNAME","YOUR_PASSWORD");    
            s =c.createStatement(); 
           
        }catch(Exception e){ 
            JOptionPane.showMessageDialog(null, "Failed to connect to the database. Please check your connection and credentials.", "Database Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }  
    }  
}
