package de.thi.mymusic.service;

import de.thi.mymusic.dao.CrudService;
import de.thi.mymusic.domain.Album;
import de.thi.mymusic.domain.BaseEntity;
import de.thi.mymusic.domain.Interpret;
import de.thi.mymusic.domain.Song;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;


/**
 * Created by Michael on 20.11.2015.
 */


@RunWith(Arquillian.class)
public class AlbumServiceIntegrationTest {

    @EJB
    AlbumService albumService;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive webarchive = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClass(BaseEntity.class)
                .addClass(AlbumService.class)
                .addClass(Album.class)
                .addClass(Song.class)
                .addClass(Interpret.class)
                .addClass(CrudService.class)
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                ;
        System.out.println(webarchive.toString(Formatters.VERBOSE));
        return webarchive;
    }


    @Test
    public void ThatAlbumCanBeAdded() {
        Album album = new Album();
        //album.addSong(new Song(1, ""));
    }
}