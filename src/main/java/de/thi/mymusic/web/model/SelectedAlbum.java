package de.thi.mymusic.web.model;

import de.thi.mymusic.domain.Album;
import de.thi.mymusic.domain.Interpret;
import de.thi.mymusic.domain.Song;
import de.thi.mymusic.repository.Repository;
import de.thi.mymusic.service.AlbumService;

import javax.faces.application.FacesMessage;
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
    private Interpret interpret;
    private String newSongTitle;
    private String newSongDuration;
    private long newSongNumber;
    private AlbumService albumService;

    //For Detail View
    private long albumId;

    //For Add/Edit View
    private boolean isEditorDetailView = false;

    @Inject
    public SelectedAlbum(AlbumService albumService){
        this.albumService = albumService;
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
                isEditorDetailView = true;
            } else {
                //TODO Translate Message String
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Album konnte nicht gefunden werden!", null);
                FacesContext.getCurrentInstance().addMessage(null, message);

                return "error";
            }
        }

        return "";
    }

    public String doSave() {
        album.setInterpret(interpret);
        albumService.add(this.album);

        return "detailAlbum.xhtml?faces-redirect=true&album="+album.getId();
    }

    public String doAddSong() {
        System.out.println("Add Song");

        this.album.addSong(new Song(this.newSongNumber, this.newSongTitle, this.newSongDuration, this.album));
        this.newSongTitle=null;
        this.newSongDuration=null;

        // Inkrement SongNumber
        this.newSongNumber++;
        return null;
    }

    public String doEditSong() {
        return "";
    }

    public String dodeleteSong() {
        return "";
    }

    public String doCancel() {
        interpret = new Interpret();
        album = new Album();
        newSongNumber = 1;
        newSongDuration = "";
        newSongTitle = "";

        return "add.xhtml";
    }

    public String doDelete() {

        albumService.delete(album);

        return "search.xhtml";
    }

}
