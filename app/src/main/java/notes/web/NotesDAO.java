package notes.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NotesDAO {
    public boolean addNote(String username, String title, String content) throws SQLException {
        try(Connection conn=DBConnection.getConnection()) {
            String checkExists="SELECT * FROM notes WHERE username=? AND title=?";
            try(PreparedStatement ps=conn.prepareStatement(checkExists)) {
                ps.setString(1, username);
                ps.setString(2, title);
                try(ResultSet rs=ps.executeQuery()) {
                    if(rs.next()) {
                        return false;
                    }
                }
            }

            String addNote="INSERT INTO notes (username, title, content) VALUES (?,?,?)";
            try(PreparedStatement ps=conn.prepareStatement(addNote)) {
                ps.setString(1, username);
                ps.setString(2, title);
                ps.setString(3, content);
                ps.executeUpdate();
                return true;
            }
        }
    }
}