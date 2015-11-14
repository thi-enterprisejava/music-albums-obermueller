package de.thi.mymusic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Functional data model of an album
 */
public class Album implements Serializable {

    private String title;
    private Interpret interpret;
    private List<Song> songs = new ArrayList<>();
    private int releaseYear;

    //************************************************
    // Constructors
    //************************************************

    public Album() {

    }

    public Album(String title, Interpret interpret, List<Song> songs, int releaseYear) {
        this.title = title;
        this.interpret = interpret;
        this.songs = songs;
        this.releaseYear = releaseYear;
    }

    //************************************************
    // Getter and Setter
    //************************************************

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Interpret getInterpret() {
        return interpret;
    }

    public void setInterpret(Interpret interpret) {
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

    //************************************************
    // Equals and HashCode
    //************************************************


    //TODO Java Objects für Equals und HashCode verwenden! (V7)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Album album = (Album) o;

        if (releaseYear != album.releaseYear) return false;
        if (title != null ? !title.equals(album.title) : album.title != null) return false;
        if (interpret != null ? !interpret.equals(album.interpret) : album.interpret != null) return false;
        return !(songs != null ? !songs.equals(album.songs) : album.songs != null);

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (interpret != null ? interpret.hashCode() : 0);
        result = 31 * result + (songs != null ? songs.hashCode() : 0);
        result = 31 * result + releaseYear;
        return result;
    }
}
