package notes.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static Connection conn=null;

    public static Connection getConnection() throws SQLException {
        if(conn==null || conn.isClosed()) {
            Properties props=new Properties();
            String path=System.getProperty("configPath", "config/db.properties");
            try(FileInputStream fis=new FileInputStream(path)) {
                props.load(fis);
            } catch(IOException e) {
                System.err.println("Could not load DB config: "+e.getMessage());
                throw new SQLException("Failed to load DB config");
            }

            String url=props.getProperty("db.url");
            String user=props.getProperty("db.user");
            String pass=props.getProperty("db.pass");

            try {
                conn=DriverManager.getConnection(url, user, pass);
                System.out.println("Connected to PostgreSQL");
            } catch(SQLException e) {
                System.err.println("Connection failed: "+e.getMessage());
                throw e;
            }
        }
        return conn;
    }
}