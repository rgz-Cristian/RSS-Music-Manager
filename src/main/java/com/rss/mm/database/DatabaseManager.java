package com.rss.mm.database;

import com.rss.mm.model.Song;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:data.db";
    
    public static Connection getConnection() throws SQLException {
        File dbFile = new File("data.db");
        boolean isNewDatabase = !dbFile.exists();

        try {
            Connection conn = DriverManager.getConnection(URL);
            if (isNewDatabase) {
                createTableIfNotExists();
            }
            return conn;
        } catch (SQLException e) {
            throw e;
        }
    }

    // CREATE
    public static void createTableIfNotExists() throws SQLException{
        String sql;
        sql = """
              CREATE TABLE IF NOT EXISTS songs (
                  id TEXT PRIMARY KEY,
                  title TEXT,
                  artist TEXT,
                  votes INTEGER
              )""";
        
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.executeUpdate();
            
        } catch (SQLException ex) {
            throw ex;
        }
    }
    // UPDATE
    public static void insertSong(Song song) throws SQLException{
        String sql;
        sql = """
              INSERT INTO songs (id, title, artist, votes)
              VALUES (?, ?, ?, ?);
              """;
        
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, song.getId());
            stmt.setString(2, song.getTitle());
            stmt.setString(3, song.getArtist());
            stmt.setInt(4, song.getVotes());


            stmt.executeUpdate();
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
        
        
    }
    
    public static void updateVotes(String id, int newVotes) throws SQLException {
        String sql = """
                UPDATE songs 
                SET votes = ?
                WHERE id = ?;
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newVotes);
            stmt.setString(2, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }
    }
    
    public static void updateSong(Song song) throws SQLException {
        String sql = """
                UPDATE songs 
                SET title = ?, artist = ?
                WHERE id = ?;
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, song.getTitle());
            stmt.setString(2, song.getArtist());
            stmt.setString(3, song.getId());

            
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }
    }
    // READ
    public static List<Song> getAllSongs() throws SQLException {
        List<Song> songs = new ArrayList<>();
        String sql = """
                    SELECT id, title, artist, votes
                    FROM songs
                    ORDER BY votes DESC;
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Mapear cada fila a un objeto Song
                Song song = new Song(
                    rs.getString("id"),
                    rs.getString("title"),
                    rs.getString("artist"),
                    rs.getInt("votes")
                );
                songs.add(song);
            }

        } catch (SQLException e) {
            throw e;
        }

        return songs;
    }
    public static List<Song> findSongs(String findText) throws SQLException {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT id, title, artist, votes FROM songs WHERE title = ? ORDER BY DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            stmt.setString(1, findText);
            
            while (rs.next()) {
                // Mapear cada fila a un objeto Song
                Song song = new Song(
                    rs.getString("id"),
                    rs.getString("title"),
                    rs.getString("artist"),
                    rs.getInt("votes")
                );
                songs.add(song);
            }

        } catch (SQLException e) {
            throw e;
        }

        return songs;
    }
    // DELETE
    public static void deleteSong(String id) throws SQLException {
        String sql = """
                     DELETE FROM songs WHERE id = ?;
                     """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
   
            
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }
    }
}