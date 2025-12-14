package com.rss.mm.controllers;

import com.rss.mm.database.DatabaseManager;
import com.rss.mm.model.Song;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Operations {

    private List<Song> songs;

    public Operations() {
        this.songs = new ArrayList<>();
        cargarCancionesIniciales();
    }

    private void cargarCancionesIniciales() {
        try {
            this.songs = DatabaseManager.getAllSongs();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar las canciones");
            this.songs = new ArrayList<>();
        }
    }
    
    public void updateVotes(String id, int votes) throws SQLException {
        try {
            DatabaseManager.updateVotes(id, votes);
        } catch(SQLException e){
            throw e;
        }
            
    }

    public boolean updateSong(Song song) {
        try {
            DatabaseManager.updateSong(song); // método que ya actualiza toda la canción en BD

            // actualizar en memoria
            for (int i = 0; i < songs.size(); i++) {
                if (songs.get(i).getId().equals(song.getId())) {
                    songs.set(i, song); // reemplaza la canción
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error actualizando canción en BD: " + e.getMessage());
        }
        return false;
    }

    public boolean addSong(Song song) {
        try {
            DatabaseManager.insertSong(song);
            songs.add(song);
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error agregando canción: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteSong(Song song) {
        try {
            DatabaseManager.deleteSong(song.getId());
            songs.remove(song);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Song> getSongs() {
        cargarCancionesIniciales();
        return new ArrayList<>(songs); // devolver copia para seguridad
    }


    public List<Song> searchSongs(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return getSongs();
        }

        List<Song> resultados = new ArrayList<>();
        String busqueda = searchText.toLowerCase();

        for (Song song : songs) {
            if (song.getTitle().toLowerCase().contains(busqueda) ||
                song.getArtist().toLowerCase().contains(busqueda)) {
                resultados.add(song);
            }
        }

        return resultados;
    }

    public void refreshDesdeBD() {
        cargarCancionesIniciales();
    }

    public Song findSongById(String id) {
        for (Song song : songs) {
            if (song.getId().equals(id)) {
                return song;
            }
        }
        return null;
    }
    
    public int getPositionByVotes(Song song) {
        // Hacemos una copia de la lista para no modificar la lista original
        List<Song> sorted = new ArrayList<>(songs);

        // Ordenar por votos descendentes (mayor a menor)
        sorted.sort((s1, s2) -> Integer.compare(s2.getVotes(), s1.getVotes()));

        // Buscar el índice de la canción
        int index = sorted.indexOf(song);

        // Si no se encuentra, devolvemos -1
        if (index < 0) {
            return -1;
        }

        // Convertimos el índice al ranking (posición)
        return index + 1;
    }

    public boolean existsByTitleAndArtist(String songName, String artistName) throws SQLException {
        return DatabaseManager.existsByTitleAndArtist(artistName, artistName);
    }
    
}
