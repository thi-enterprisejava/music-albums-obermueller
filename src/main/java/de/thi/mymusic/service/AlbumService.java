package de.thi.mymusic.service;

import de.thi.mymusic.domain.Album;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Michael Obermüller on 14.11.2015.
 */

@ApplicationScoped
public class AlbumService {

    @PersistenceContext(unitName="mymusic")
    private EntityManager em;

    @Transactional(Transactional.TxType.REQUIRED)
    public void add(Album album) {
        em.persist(album);
    }

    public Album findById(long id) {
        return em.find(Album.class, id);
    }

    public List<Album> findByName(String name) {
        Query q = em.createQuery("SELECT a from" +
                " Album as a WHERE UPPER(a.title) like CONCAT('%', :name, '%')");
        q.setParameter("name", name.toUpperCase());
        return  q.getResultList();
    }

}
