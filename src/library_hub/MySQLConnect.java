package library_hub;


import java.sql.*;
import java.sql.Connection;
import javax.swing.JOptionPane;

public class MySQLConnect {
    private static final String URL = "jdbc:mysql://localhost:3306/libraryhub";
    private static final String USER = "root";
    private static final String PASS = "";
    
    public static Connection getConnection(){
        Connection conn = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASS);
            //JOptionPane.showMessageDialog(null, PASS);
            
        }catch(ClassNotFoundException | SQLException e){
            JOptionPane.showMessageDialog(null, PASS);
        }
        return conn;
    }
}
