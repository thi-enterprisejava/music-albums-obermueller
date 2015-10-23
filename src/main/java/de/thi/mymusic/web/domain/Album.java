package de.thi.mymusic.web.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Functional data model of an album
 */
public class Album implements Serializable {

    private String title;
    private String interpret;
    private List<Song> songs = new ArrayList<>();
    private int releaseYear;

    //************************************************
    // Getter and Setter
    //************************************************

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInterpret() {
        return interpret;
    }

    public void setInterpret(String interpret) {
        this.interpret = interpret;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public void addSong(Song song) {
        this.songs.add(song);
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }


}
