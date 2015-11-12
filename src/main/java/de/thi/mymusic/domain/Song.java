package de.thi.mymusic.domain;

import java.io.Serializable;

/**
 * Functional data model of a song
 */
public class Song implements Serializable {

    private long songNumber;
    private String title;
    // String with Format mm:ss
    //private String duration;
    private long duration;

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

        if (songNumber != song.songNumber) return false;
        if (duration != song.duration) return false;
        return !(title != null ? !title.equals(song.title) : song.title != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (songNumber ^ (songNumber >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (int) (duration ^ (duration >>> 32));
        return result;
    }
}
