package notes.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public boolean registerUser(String username, String password) throws SQLException {
        try(Connection conn=DBConnection.getConnection()) {
            String checkExists="SELECT * FROM users WHERE username=?";
            try(PreparedStatement ps=conn.prepareStatement(checkExists)) {
                ps.setString(1, username);
                try(ResultSet rs=ps.executeQuery()) {
                    if(rs.next()) {
                        return false;
                    }
                }
            }

            String regUser="INSERT INTO users (username, password) VALUES (?, ?)";
            try(PreparedStatement ps=conn.prepareStatement(regUser)) {
                ps.setString(1, username);
                ps.setString(2, password);
                ps.executeUpdate();
                return true;
            }
        } 
    }
}