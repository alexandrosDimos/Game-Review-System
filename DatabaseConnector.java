package sample;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnector {
    static Connection databaseLink = null;

    public Connection getConnection(){
        String databaseUser = "root";
        String databasePassword = "your_password";
        String url = "jdbc:mysql://localhost:3306/jdb_demo";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        }catch(Exception e){
            System.out.println(e);
        }

        try{
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        }catch (Exception e){
            System.out.println(e);
        }
        return databaseLink;
    }
}
//jdbc:mysql://localhost:3306/?user=root