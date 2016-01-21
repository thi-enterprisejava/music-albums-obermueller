package de.thi.mymusic.service;

import de.thi.mymusic.domain.Album;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 03.12.2015.
 */
@WebService
@Stateless
public class AlbumWebService {

    @EJB
    private AlbumService albumService;

    @WebMethod
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public List<Album> findAll() {
        List<Album> albums = albumService.findAll();
        albums.forEach(this::clearAlbum);

        return albums;
    }

    @WebMethod
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Album findById(@WebParam(name="id") Long id) {
        Album album = albumService.findById(id);
        clearAlbum(album);

        return album;
    }

    private void clearAlbum(Album album) {
        album.getSongs().forEach(song -> song.setAlbum(null));
        album.getInterpret().setAlbums(new ArrayList(0));

    }

}
