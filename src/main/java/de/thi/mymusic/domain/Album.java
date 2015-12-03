package de.thi.mymusic.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Functional data model of an album
 */

@Entity
@NamedQueries({
        @NamedQuery(name = "Album.findAll",
                query = "SELECT a FROM Album a"),
        @NamedQuery(name = "Album.findByName",
                query = "SELECT a FROM Album a WHERE UPPER(a.title) like CONCAT('%', Upper(:name), '%')"),
})
public class Album extends BaseEntity {

    @NotNull
    private String title;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @NotNull
    private Interpret interpret;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "album", fetch = FetchType.EAGER)
    @OrderBy("songNumber ASC")
    private List<Song> songs = new ArrayList<>();
    private int releaseYear;

    private String imageFilename;

    //************************************************
    // Constructors
    //************************************************

    public Album() {

    }

    public Album(String title, Interpret interpret, List<Song> songs, int releaseYear) {
        this.title = title;
        this.interpret = interpret;
        this.songs = songs;

        for(int i=0; i < songs.size(); i++) {
            songs.get(0).setAlbum(this);
        }
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

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getNumberOfSongs() {
        return songs.size();
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }

    public void addSong(Song song) {
        song.setAlbum(this);
        if(songs.size() >=  song.getSongNumber()) {
            songs.set((int) song.getSongNumber() - 1, song);
        } else {
            this.songs.add((int) song.getSongNumber() - 1, song);
        }
    }

    public void removeSong(Song song) {
        this.songs.remove(song);
    }

    public String formatedTotalDuration() {
        long totalDuration = songs
                     .stream()
                     .mapToLong(s -> s.getDuration())
                     .sum();
        return String.format("%02d:%02d", totalDuration / 60, totalDuration % 60);
    }

    //************************************************
    // Equals and HashCode
    //************************************************

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
