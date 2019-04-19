package Connectivity;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionClass {
public Connection connection;

    public Connection getConnection()
    {
        String dbname = "airportdb";
        String username = "root";
        String password = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection("jdbc:mysql://localhost/"+dbname,username,password);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

//    public static void main(String [] args)
//    {
//        ConnectionClass conclass = new ConnectionClass();
//        Connection con;
//        con = conclass.getConnection();
//
//    }
}
