package de.thi.mymusic.service;

import de.thi.mymusic.domain.Album;

import javax.ejb.EJB;
import javax.ws.rs.*;
import java.util.List;

/**
 * Created by Michael on 03.12.2015.
 */

@Path("/albums")
public class AlbumRestService {

    @EJB
    AlbumService albumService;

    @GET
    @Produces("application/json")
    public List<Album> findAll() {
        return albumService.findAll();
    }

    @GET
    @Produces("application/json")
    @Path("/{albumId}")
    public Album findById(@PathParam("albumId") Long id) {
        Album album =  albumService.findById(id);
        album.setInterpret(null);

        return album;
    }

    @POST
    @Consumes("application/json")
    public Album post(Album album) {
        return albumService.saveOrUpdate(album);
    }

    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    @Path("{albumId}")
    public Album update(Album album) {

        //TODO Implement REST - PUT
        albumService.saveOrUpdate(album);

        return album;
    }


    @DELETE
    @Path("{albumId}")
    public void deleteAlbum(@PathParam("albumId") Long id) {
        Album album = albumService.findById(id);
        if(album != null) {
            albumService.delete(album);
        }
    }

}
