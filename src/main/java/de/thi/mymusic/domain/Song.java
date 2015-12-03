package de.thi.mymusic.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.Objects;

/**
 * Functional data model of a song
 */

@Entity
@NamedQueries({
        @NamedQuery(name = "Song.findByName",
                query = "SELECT s FROM Song s WHERE  UPPER(s.title) like CONCAT('%', UPPER(:name), '%')"),
})
public class Song extends BaseEntity {

    private long songNumber;
    private String title;
    private long duration;

    @ManyToOne
    @JsonIgnore
    private Album album;

    //**********************************************
    // Constructors
    //**********************************************

    public Song()
    {

    }

    public Song(long songNumber, String title, String formattedDuration) {
        this.songNumber = songNumber;
        this.title = title;
        this.setFormattedDuration(formattedDuration);
    }

    public Song(long songNumber, String title, long duration) {
        this.songNumber = songNumber;
        this.title = title;
        this.duration = duration;
    }

    public Song(long songNumber, String title, String formattedDuration, Album album) {
        this.songNumber = songNumber;
        this.title = title;
        this.setFormattedDuration(formattedDuration);
        this.album = album;
    }

    public Song(long songNumber, String title, long duration, Album album) {
        this.songNumber = songNumber;
        this.title = title;
        this.duration = duration;
        this.album = album;
    }


    //**********************************************
    // Getter and Setter
    //**********************************************

    public long getSongNumber() {
        return songNumber;
    }

    public void setSongNumber(long songNumber) {
        this.songNumber = songNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    // Convert and set song duration in seconds from a mm:ss formatted string
    public void setFormattedDuration(String formattedDuration) {
        if(formattedDuration != null && formattedDuration.length() == 5) {
            String[] splittedDuration = formattedDuration.split(":");
            long convertedDuration = Long.valueOf(splittedDuration[0]) * 60;
            convertedDuration = convertedDuration + Long.valueOf(splittedDuration[1]);
            this.duration = convertedDuration;
        }
    }


    // Returns the duration time in seconds as String in format: mm:ss
    public String getFormattedDuration() {
        return String.format("%02d:%02d", duration / 60, duration % 60);
    }

    //************************************************
    // Equals and HashCode
    //************************************************


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return songNumber == song.songNumber &&
                duration == song.duration &&
                Objects.equals(title, song.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songNumber, title, duration);
    }
}
