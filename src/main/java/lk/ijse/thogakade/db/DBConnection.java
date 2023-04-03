package lk.ijse.thogakade.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static final String URL="jdbc:mysql://localhost:3306/thogakade";
    static Properties props =new Properties();
    private static DBConnection connection;
    private Connection con;

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "80221474");
    }

    private DBConnection() throws SQLException {
         con= DriverManager.getConnection(URL,props);
    }

    public static DBConnection getInstance() throws SQLException {
        return (connection==null)? new DBConnection() : connection;
    }

    public Connection getConnection(){
        return con;
    }

}
