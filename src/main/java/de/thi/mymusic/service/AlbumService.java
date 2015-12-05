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

    public Album saveOrUpdate(Album album, Interpret interpret, String imageName) {
        /**
         * Check if interpret with same name already exists
         */
        List<Interpret> interprets = this.findInterpretByExactName(interpret.getName());
        if(interprets != null && interprets.size() > 0 ) {
            interpret = interprets.get(0);
        }

        interpret.addAlbum(album);

        if((album.getImageFilename() != null && imageName == null)
                || (imageName != null && !imageName.equals(album.getImageFilename()))) {
            FileUtils.deleteFile(FileUtils.IMAGE_PATH + File.separator + album.getImageFilename());
            logger.info("Delete File: " + album.getImageFilename());
        }

        album.setImageFilename(imageName);

        if(0 == album.getId()) {
           save(album, interpret);
        } else {
           update(album, interpret);
        }

        return album;
    }

    private void save(Album album, Interpret interpret) {
        album.setInterpret(interpret);
        crudService.persist(album);
        logger.info("Save-Album: " + album.getTitle());
    }

    private void update(Album album, Interpret interpret) {
        Interpret oldInterpret = crudService.findById(Interpret.class, album.getInterpret().getId());
        if(oldInterpret.getAlbums().size() == 1){
            crudService.delete(oldInterpret);
        }

        album.setInterpret(crudService.merge(interpret));

        Album oldAlbum = findById(album.getId());
        for(Song song : oldAlbum.getSongs()) {
            if(!album.getSongs().contains(song)) {
                crudService.delete(song);
            }
        }

        crudService.merge(album);
        logger.info("Update-Album: " + album.getTitle());
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
