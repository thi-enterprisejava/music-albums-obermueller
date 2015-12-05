package de.thi.mymusic.service;

import de.thi.mymusic.dao.CrudService;
import de.thi.mymusic.domain.Album;
import de.thi.mymusic.domain.Interpret;
import de.thi.mymusic.domain.Song;
import de.thi.mymusic.util.FileUtils;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.File;
import java.util.List;

/**
 * Created by Michael Obermüller on 14.11.2015.
 */

@Stateless
public class AlbumService {

    @EJB
    private CrudService crudService;

    private static final Logger logger = Logger.getLogger(AlbumService.class);

    public Album saveOrUpdate(Album album) {
        /**
         * Check if interpret with same name already exists
         */
        List<Interpret> interprets = this.findInterpretByExactName(album.getInterpret().getName());
        if(interprets != null && interprets.size() > 0 ) {
            album.setInterpret(interprets.get(0));
        }

        if(0 == album.getId()) {
           save(album);
        } else {
           update(album);
        }

        return album;
    }

    private void save(Album album) {
        crudService.persist(album);
        logger.info("Save-Album: " + album.getTitle());
    }

    private void update(Album album) {
        Album oldAlbum = crudService.findById(Album.class, album.getId());
        Interpret oldInterpret = crudService.findById(Interpret.class, oldAlbum.getInterpret().getId());

        // Delete old Image file
        deleteOldImageFileIfChanged(oldAlbum, album);

        // Delete interpret if only this album was mapped
        if(oldInterpret.getAlbums().size() == 1){
            crudService.delete(oldInterpret);
        }

        // Delete old songs which now aren´t mapped with updated album
        for(Song song : oldAlbum.getSongs()) {
            if(!album.getSongs().contains(song)) {
                crudService.delete(song);
            }
        }

        crudService.merge(album);
        logger.info("Update-Album: " + album.getTitle());
    }

    private void deleteOldImageFileIfChanged(Album oldAlbum, Album updatedAlbum) {
        if((oldAlbum.getImageFilename() != null && updatedAlbum.getImageFilename() == null)
                || (updatedAlbum.getImageFilename() != null && !updatedAlbum.getImageFilename().equals(oldAlbum.getImageFilename()))) {
            FileUtils.deleteFile(FileUtils.IMAGE_PATH + File.separator + oldAlbum.getImageFilename());
            logger.info("Delete File: " + updatedAlbum.getImageFilename());
        }
    }

    public Album findById(long id) {
        return crudService.findById(Album.class, id);
    }

    public List<Album> findAll(){
        return crudService.findAll(Album.class);
    }

    public List<Interpret> findInterpretByExactName(String name) {
        return crudService.findByNamedQuery(Interpret.class,"Interpret.findByExactName",
                new String[] {"name"}, new Object[] {name});
    }

    public void delete(Album album) {
        Interpret interpret = crudService.findById(Interpret.class, album.getInterpret().getId());

        if(album.getImageFilename() != null) {
            FileUtils.deleteFile(FileUtils.IMAGE_PATH + album.getImageFilename());
        }

        logger.info("Delete-Album: " + album.getTitle());

        crudService.delete(album);
        if(interpret.getAlbums().size() == 1) {
            crudService.delete(interpret);
        }
    }

    /*public void deleteSong(Song song) {
        Album album = song.getAlbum();
        album.removeSong(song);
        crudService.merge(album);
        Song songMerged = crudService.merge(song);
        crudService.delete(songMerged);
    }*/

}
