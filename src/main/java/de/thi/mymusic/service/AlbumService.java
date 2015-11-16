package de.thi.mymusic.service;

import de.thi.mymusic.dao.CrudService;
import de.thi.mymusic.domain.Album;

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
        crudService.persist(album);
    }

    public Album findById(long id) {
        return crudService.findById(Album.class, id);
    }

}
