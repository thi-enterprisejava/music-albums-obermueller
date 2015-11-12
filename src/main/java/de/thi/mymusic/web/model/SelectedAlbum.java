package de.thi.mymusic.web.model;

import de.thi.mymusic.domain.Album;
import de.thi.mymusic.domain.Interpret;
import de.thi.mymusic.domain.Song;
import de.thi.mymusic.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Michael on 23.10.2015.
 */
@Named
@ViewScoped
public class SelectedAlbum implements Serializable
{

    private Album album;
    private Interpret interpret;
    private String newSongTitle;
    private String newSongDuration;
    private long newSongNumber;
    private Repository albumRepository;

    //For Detail View
    private String albumTitle;

    @Inject
    public SelectedAlbum(Repository<Album> albumRepository){
        this.albumRepository = albumRepository;
        interpret = new Interpret();
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

    public Interpret getInterpret() {
        return interpret;
    }

    public void setInterpret(Interpret interpret) {
        this.interpret = interpret;
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

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }


    //*******************************************************
    // Action Methods
    //*******************************************************

    // TODO Implement Finding by ID instead of Name (After DB-Implementation)
    public void init(){
        List<Album> result = albumRepository.findByName(albumTitle);

        // Take first list element
        album = result.get(0);
    }

    public String doSave() {
        album.setInterpret(interpret);
        albumRepository.add(this.album);

        return "detailAlbum.xhtml?faces-redirect=true&album="+album.getTitle();
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
