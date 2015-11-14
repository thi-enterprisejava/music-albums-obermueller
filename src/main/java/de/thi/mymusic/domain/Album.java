package de.thi.mymusic.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Functional data model of an album
 */

@Entity
public class Album implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long Id;
    private String title;

    @ManyToOne(cascade = CascadeType.ALL)
    private Interpret interpret;

    @OneToMany(cascade = CascadeType.ALL)
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


    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

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
        return releaseYear == album.releaseYear &&
                Objects.equals(title, album.title) &&
                Objects.equals(interpret, album.interpret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, interpret, releaseYear);
    }
}
