package de.thi.mymusic.service;

import de.thi.mymusic.dao.CrudService;
import de.thi.mymusic.domain.Album;
import de.thi.mymusic.domain.Interpret;
import de.thi.mymusic.util.FileUtils;
import org.apache.log4j.Logger;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by Michael Obermüller on 14.11.2015.
 */
@Stateless
public class AlbumService {

    @EJB
    private CrudService crudService;

    @EJB
    private FileUtils fileUtils;

    private static final Logger LOGGER = Logger.getLogger(AlbumService.class);

    public void setCrudService(CrudService crudService) {
        this.crudService = crudService;
    }

    public void setFileUtils(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    @RolesAllowed("User")
    public Album createOrUpdate(Album album) {
         // Check if interpret with same name already exists
        List<Interpret> interprets = this.findInterpretByExactName(album.getInterpret().getName());
        if(interprets != null && !interprets.isEmpty()) {
            album.setInterpret(interprets.get(0));
        }

        if(0 == album.getId()) {
           save(album);
        } else {
           update(album);
        }

        return album;
    }

    @RolesAllowed("User")
    private void save(Album album) {
        crudService.persist(album);
        LOGGER.info("Save-Album: " + album.getTitle());
    }

    @RolesAllowed("User")
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
        oldAlbum.getSongs().forEach(song-> {
            if(!album.getSongs().contains(song)) {
                crudService.delete(song);
            }
        });

        crudService.merge(album);
        LOGGER.info("Update-Album: " + album.getTitle());
    }

    @RolesAllowed("User")
    private void deleteOldImageFileIfChanged(Album oldAlbum, Album updatedAlbum) {
        if((oldAlbum.getImageFilename() != null && updatedAlbum.getImageFilename() == null)
                || (updatedAlbum.getImageFilename() != null && !updatedAlbum.getImageFilename().equals(oldAlbum.getImageFilename()))) {
            fileUtils.deleteFile(oldAlbum.getImageFilename());
            LOGGER.info("Delete File: " + updatedAlbum.getImageFilename());
        }
    }

    @PermitAll
    public Album findById(long id) {
        return crudService.findById(Album.class, id);
    }

    @PermitAll
    public List<Album> findAll(){
        return crudService.findAll(Album.class);
    }

    @PermitAll
    public List<Interpret> findInterpretByExactName(String name) {
        return crudService.findByNamedQuery(Interpret.class,"Interpret.findByExactName",
                new String[] {"name"}, new Object[] {name});
    }

    @RolesAllowed("User")
    public void delete(Album album) {
        Interpret interpret = crudService.findById(Interpret.class, album.getInterpret().getId());

        if(album.getImageFilename() != null) {
            fileUtils.deleteFile(album.getImageFilename());
        }

        LOGGER.info("Delete-Album: " + album.getTitle());

        crudService.delete(album);
        if(interpret.getAlbums().size() == 1) {
            crudService.delete(interpret);
        }
    }
}
