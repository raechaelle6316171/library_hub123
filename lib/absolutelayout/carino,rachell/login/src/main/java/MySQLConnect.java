/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CarinoCRUD;
import java.sql.*;
import javax.swing.JOptionPane;
/**
 *
 * @author RACHELL
 */
public class MySQLConnect {
    private static final String URL = "jdbc:mysql://localhost:3306/carino";
    private static final String USER = "root";
    private static final String PWD = "";
    
    public static Connection getConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER,PWD);
            
        }catch 
    }
}
