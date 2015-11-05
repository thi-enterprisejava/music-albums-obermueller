package de.thi.mymusic.domain;

import java.io.Serializable;

/**
 * Functional data model of a song
 */
public class Song implements Serializable {

    private long songNumber;
    private String title;
    // String with Format mm:ss
    private String duration;

    //**********************************************
    // Constructors
    //**********************************************

    public Song()
    {

    }

    public Song(long songNumber, String title, String duration) {
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
