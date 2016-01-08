package de.thi.mymusic.service;

import de.thi.mymusic.dao.CrudService;
import de.thi.mymusic.domain.Album;
import de.thi.mymusic.domain.Interpret;
import de.thi.mymusic.domain.SearchResultEntity;
import de.thi.mymusic.domain.Song;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 16.11.2015.
 */
@Stateless
public class SearchService {

    @EJB
    private CrudService crudService;

    public void setCrudService(CrudService crudService) {
        this.crudService = crudService;
    }

    @PermitAll
    public List<Album> findAlbumByName(String name) {
        return  crudService.findByNamedQuery(Album.class,"Album.findByName",new String[] {"name"}, new Object[] {name} );
    }

    @PermitAll
    public List<Song> findSongByName(String name) {
        return  crudService.findByNamedQuery(Song.class,"Song.findByName",new String[] {"name"}, new Object[] {name} );
    }

    @PermitAll
    public List<Interpret> findInterpretByName(String name) {
        return  crudService.findByNamedQuery(Interpret.class,"Interpret.findByName",new String[] {"name"}, new Object[] {name} );
    }

    /**
     * FindEntitiesByName search for interprets, songs and albums which have param name as part of their name
     * and returns this entities
     *
     * @param name of interpret, song or album
     * @return List of max five SearchResultEntity first elements are albums, interprets then songs
     */
    @PermitAll
    public List<SearchResultEntity> findEntitiesByName(String name) {
        EntityManager em = crudService.getEntityManager();
        List<SearchResultEntity> result = new ArrayList<>();

        // List of search queries
        List<String> queries = new ArrayList<>();
        // Album query
        queries.add("SELECT NEW de.thi.mymusic.domain.SearchResultEntity(a.title, 'Album') FROM Album as a WHERE UPPER(a.title) like CONCAT('%', Upper(:name), '%')");
        // Interpret query
        queries.add("SELECT NEW de.thi.mymusic.domain.SearchResultEntity(i.name, 'Interpret') FROM Interpret as i WHERE UPPER(i.name) like CONCAT('%', Upper(:name), '%')");
        // Song query
        queries.add("SELECT NEW de.thi.mymusic.domain.SearchResultEntity(s.title, 'Song') FROM Song as s WHERE UPPER(s.title) like CONCAT('%', Upper(:name), '%')");

        queries.forEach(query -> {
            TypedQuery<SearchResultEntity> typedQuery = em.createQuery(query, SearchResultEntity.class);
            typedQuery.setMaxResults(5);
            typedQuery.setParameter("name", name);
            List<SearchResultEntity> resultList = typedQuery.getResultList();
            if(resultList != null) {
                typedQuery.getResultList().forEach(searchResultEntity -> {
                    if (result.size() < 5) {
                        result.add(searchResultEntity);
                    }
                });
            }
        });

        return result;
    }

}
