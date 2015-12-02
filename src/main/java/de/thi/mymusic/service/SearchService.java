package de.thi.mymusic.service;

import de.thi.mymusic.dao.CrudService;
import de.thi.mymusic.domain.Album;
import de.thi.mymusic.domain.Interpret;
import de.thi.mymusic.domain.Song;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by Michael on 16.11.2015.
 */

@Stateless
public class SearchService {

    @EJB
    private CrudService crudService;

    public List<Album> findAlbumByName(String name) {
        return  crudService.findByNamedQuery(Album.class,"Album.findByName",new String[] {"name"}, new Object[] {name} );
    }

    public List<Song> findSongByName(String name) {
        return  crudService.findByNamedQuery(Song.class,"Song.findByName",new String[] {"name"}, new Object[] {name} );
    }

    public List<Interpret> findInterpretByName(String name) {
        return  crudService.findByNamedQuery(Interpret.class,"Interpret.findByName",new String[] {"name"}, new Object[] {name} );
    }

}
