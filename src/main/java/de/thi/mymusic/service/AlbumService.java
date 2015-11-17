package de.thi.mymusic.service;

import de.thi.mymusic.dao.CrudService;
import de.thi.mymusic.domain.Album;
import de.thi.mymusic.domain.Interpret;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Michael Obermüller on 14.11.2015.
 */

@ApplicationScoped
public class AlbumService {

    @Inject
    private CrudService crudService;

    @Transactional
    public void add(Album album) {

        /**
         * Check if interpret with same name already exists
         */
        List<Interpret> interprets = this.findInterpretByExactName(album.getInterpret().getName());
        if(interprets != null && interprets.size() > 0) {
            album.setInterpret(interprets.get(0));
        }

        crudService.persist(album);
    }

    public Album findById(long id) {
        return crudService.findById(Album.class, id);
    }

    public List<Interpret> findInterpretByExactName(String name) {
        return crudService.findByNamedQuery(Interpret.class,"Interpret.findByExactName", new String[] {"name"}, new Object[] {name});
    }

    @Transactional
    public void delete(Album album) {

        Interpret interpret = crudService.findById(Interpret.class, album.getInterpret().getId());

        crudService.delete(album);

        /*if(interpret.getAlbums().size() == 0) {
            crudService.delete(interpret);
        }*/
    }

}
