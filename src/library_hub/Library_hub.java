package library_hub;

import java.sql.Connection;

public class Library_hub {

    public static void main(String[] args) {

        Connection conn = MySQLConnect.getConnection();
    }
    
}
