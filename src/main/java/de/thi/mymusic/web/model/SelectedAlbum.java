package de.thi.mymusic.web.model;

import de.thi.mymusic.domain.Album;
import de.thi.mymusic.domain.Interpret;
import de.thi.mymusic.domain.Song;
import de.thi.mymusic.service.AlbumService;
import de.thi.mymusic.util.FileUtils;
import org.apache.log4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import java.io.*;
import java.util.UUID;

/**
 * Created by Michael on 23.10.2015.
 */
@Named
@ViewScoped
public class SelectedAlbum implements Serializable
{

    private static final Logger logger = Logger.getLogger(SelectedAlbum.class);

    private Album album;
    private Song editSong;
    private Interpret interpret;
    private String currentSongTitle;
    private String currentSongDuration;
    private long currentSongNumber;
    private AlbumService albumService;
    private Part imageFile;
    private String imageName;

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

    public Part getImageFile() {
        return imageFile;
    }

    public void setImageFile(Part imageFile) {
        this.imageFile = imageFile;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
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
                imageName = album.getImageFilename();
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
        uploadImage();

        album.setInterpret(interpret);

        albumService.createOrUpdate(this.album);

        return "detailAlbum.xhtml?faces-redirect=true&album="+album.getId();
    }

    private void uploadImage() {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            if (imageFile != null && imageFile.getSize() > 0) {
                String fileTyp = FileUtils.getFileTypFromPart(imageFile);
                String uuid = UUID.randomUUID().toString();
                imageName = uuid + fileTyp;
                File outputFile = new File(FileUtils.IMAGE_PATH + File.separator  + imageName);
                inputStream = imageFile.getInputStream();
                outputStream = new FileOutputStream(outputFile);
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }

                album.setImageFilename(imageName);
            }
        }catch(IOException ex) {
            logger.error("Image couldn´t be loaded!");
            imageName = null;
        }
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
        logger.info("Delete Song: " + song.getTitle());
        album.removeSong(song);

        return null;
    }

    public String doCancel() {
        if(albumId > 0) {
            return "edit.xhtml?album=" + albumId;
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

    public String doDeleteImage() {
        this.imageName = null;

        return null;
    }

    private void initSong() {
        currentSongNumber = album.getSongs().size() + 1;
        currentSongDuration = null;
        currentSongTitle = null;
        editSong = null;
    }

}
