package de.thi.mymusic.web.model;

import de.thi.mymusic.domain.Album;
import de.thi.mymusic.domain.Interpret;
import de.thi.mymusic.domain.Song;
import de.thi.mymusic.repository.Repository;
import de.thi.mymusic.service.AlbumService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
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
    private Song editSong;
    private Interpret interpret;
    private String currentSongTitle;
    private String currentSongDuration;
    private long currentSongNumber;
    private AlbumService albumService;

    //For Detail View
    private long albumId;


    @Inject
    public SelectedAlbum(AlbumService albumService){
        this.albumService = albumService;
        interpret = new Interpret();
        album = new Album();
        initSong();
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
        return currentSongTitle;
    }

    public void setNewSongTitle(String newSongTitle) {
        this.currentSongTitle = newSongTitle;
    }

    public String getNewSongDuration() {
        return currentSongDuration;
    }

    public void setNewSongDuration(String newSongDuration) {
        this.currentSongDuration = newSongDuration;
    }

    public long getNewSongNumber() {
        return currentSongNumber;
    }

    public void setNewSongNumber(long newSongNumber) {
        this.currentSongNumber = newSongNumber;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    //*******************************************************
    // Action Methods
    //*******************************************************

    public String init(){
        if(albumId > 0) {
            album = albumService.findById(albumId);

            if(album != null) {
                currentSongNumber = album.getSongs().size() + 1;
                interpret = album.getInterpret() ;
            } else {
                //TODO Translate Message String
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Album konnte nicht gefunden werden!", null);
                FacesContext.getCurrentInstance().addMessage(null, message);

                return "error";
            }
        }

        return null;
    }

    public String doSave() {
        albumService.saveOrUpdate(this.album, this.interpret);

        return "detailAlbum.xhtml?faces-redirect=true&album="+album.getId();
    }

    public String doAddSong() {
        Song song = null;
        if(editSong != null) {
            song = editSong;
            song.setTitle(currentSongTitle);
            song.setFormattedDuration(currentSongDuration);
        } else {
            song = new Song(currentSongNumber, currentSongTitle, currentSongDuration, album);
        }

        this.album.addSong(song);
        initSong();

        return null;
    }

    public String doPrepareEditSong(Song song) {
        this.editSong=song;
        this.currentSongNumber = song.getSongNumber();
        this.currentSongTitle = song.getTitle();
        this.currentSongDuration = song.getFormattedDuration();

        return null;
    }


    public String doDeleteSong(Song song) {
        System.out.println("Delete Song: " + song.getTitle());
        album.removeSong(song);

        return null;
    }

    public String doCancel() {
        if(editSong != null) {
            return "edit.xhtml?album=" + editSong.getId();
        }
        interpret = new Interpret();
        album = new Album();
        initSong();

        return "edit.xhtml";
    }

    public String doDelete() {

        //TODO Info Message that delete was successful
        albumService.delete(album);

        return "search.xhtml?faces-redirect=true";
    }

    private void initSong() {
        currentSongNumber = album.getSongs().size() + 1;
        currentSongDuration = null;
        currentSongTitle = null;
        editSong = null;
    }

}
