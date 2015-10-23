package de.thi.mymusic.web.model;

import de.thi.mymusic.web.domain.Album;
import de.thi.mymusic.web.domain.Song;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by Michael on 23.10.2015.
 */
@Named
@SessionScoped
public class SelectedAlbum implements Serializable
{

    private Album album;
    private String newSongTitle;
    private String newSongDuration;

    public SelectedAlbum()
    {
        album = new Album();
    }


    //*******************************************************
    // Getter and Setter
    //*******************************************************

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getNewSongTitle() {
        return newSongTitle;
    }

    public void setNewSongTitle(String newSongTitle) {
        this.newSongTitle = newSongTitle;
    }

    public String getNewSongDuration() {
        return newSongDuration;
    }

    public void setNewSongDuration(String newSongDuration) {
        this.newSongDuration = newSongDuration;
    }

    //*******************************************************
    // Action Methods
    //*******************************************************

    public String doSave() {
        return null;
    }

    public String doAddSong() {
        this.album.addSong(new Song(this.newSongTitle, this.newSongDuration));
        this.newSongTitle=null;
        this.newSongDuration=null;
        return null;
    }



}
