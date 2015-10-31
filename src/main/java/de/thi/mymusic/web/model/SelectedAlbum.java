package de.thi.mymusic.web.model;

import de.thi.mymusic.domain.Album;
import de.thi.mymusic.domain.Song;
import de.thi.mymusic.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by Michael on 23.10.2015.
 */
@Named
@ViewScoped
public class SelectedAlbum implements Serializable
{

    private Album album;
    private String newSongTitle;
    private String newSongDuration;
    private long newSongNumber;
    private Repository albumRepository;

    @Inject
    public SelectedAlbum(Repository<Album> albumRepository){
        this.albumRepository = albumRepository;
        album = new Album();
        this.newSongNumber = 1;
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

    public long getNewSongNumber() {
        return newSongNumber;
    }

    public void setNewSongNumber(long newSongNumber) {
        this.newSongNumber = newSongNumber;
    }

    //*******************************************************
    // Action Methods
    //*******************************************************

    public String doSave() {
        System.out.println("Do Save Album");
        albumRepository.add(this.album);

        return null;
    }

    public String doAddSong() {
        System.out.println("Add Song");

        this.album.addSong(new Song(this.newSongNumber, this.newSongTitle, this.newSongDuration));
        this.newSongTitle=null;
        this.newSongDuration=null;

        // Inkrement SongNumber
        this.newSongNumber++;
        return null;
    }


}
