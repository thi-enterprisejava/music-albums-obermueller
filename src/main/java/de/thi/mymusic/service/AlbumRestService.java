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
        return  albumService.findById(id);
    }

}
