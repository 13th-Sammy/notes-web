package notes.web;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        try(Connection conn=DBConnection.getConnection()) {
            Statement stmt=conn.createStatement();
            try(ResultSet rs=stmt.executeQuery("SELECT * FROM users")) {
                System.out.println(rs.next());
            }
        } catch(SQLException e) {}
    }
}