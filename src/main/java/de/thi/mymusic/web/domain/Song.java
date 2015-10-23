package de.thi.mymusic.web.domain;

/**
 * Functional model of a song
 */
public class Song {

    private String title;
    // String with Format mm:ss
    private String duration;

    //**********************************************
    // Constructors
    //**********************************************

    public Song()
    {

    }

    public Song(String title, String duration) {
        this.title = title;
        this.duration = duration;
    }


    //**********************************************
    // Getter and Setter
    //**********************************************

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
