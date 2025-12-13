/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rss.mm.model;


public class Song {
    
    private String id;
    private String title;
    private String artist;
    private int votes;

    
    public Song(String id, String name, String artist, int votes) {
        this.id = id;
        this.title = name;
        this.artist = artist;
        this.votes = votes;
    }
    


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
    
    
    @Override
    public String toString() {
        return title + "      -----      " + artist + "   ----  Votes: "+ votes;
    }

}
