package notes.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

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

    public JSONArray getNotes(String username) throws SQLException {
        JSONArray notesArray=new JSONArray();
        try(Connection conn=DBConnection.getConnection()) {
            String getNotes="SELECT * FROM notes WHERE username=?";
            try(PreparedStatement ps=conn.prepareStatement(getNotes)) {
                ps.setString(1, username);
                try(ResultSet rs=ps.executeQuery()) {
                    while(rs.next()) {
                        JSONObject note=new JSONObject();
                        note.put("title", rs.getString("title"));
                        note.put("content", rs.getString("content"));
                        notesArray.put(note);
                    }
                }
            }
        }
        return notesArray;
    }

    public boolean updateNote(String username, String oldTitle, String newTitle, String newContent) throws SQLException {
        if(!newTitle.equals(oldTitle)) {
            try(Connection conn=DBConnection.getConnection()) {
                String checkExists="SELECT * FROM notes WHERE username=? AND title=?";
                try(PreparedStatement ps=conn.prepareStatement(checkExists)) {
                    ps.setString(1, username);
                    ps.setString(2, newTitle);
                    try(ResultSet rs=ps.executeQuery()) {
                        if(rs.next()) {
                            return false;
                        }
                    }
                }
            }
        }
        
        try(Connection conn=DBConnection.getConnection()) {
            String updateNote="UPDATE notes SET title=?, content=? WHERE username=? AND title=?";
            try(PreparedStatement ps=conn.prepareStatement(updateNote)) {
                ps.setString(1, newTitle);
                ps.setString(2, newContent);
                ps.setString(3, username);
                ps.setString(4, oldTitle);
                ps.executeUpdate();
                return true;
            }
        }
    }
}