package de.thi.mymusic.service;

import de.thi.mymusic.dao.CrudService;
import de.thi.mymusic.domain.*;
import de.thi.mymusic.fixture.AlbumFixture;
import de.thi.mymusic.fixture.InterpretFixture;
import de.thi.mymusic.fixture.SongFixture;
import de.thi.mymusic.security.AuthenticatedWithRoleAdmin;
import de.thi.mymusic.security.AuthenticatedWithRoleUser;
import de.thi.mymusic.util.FileUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.ejb.EJBAccessException;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Michael on 20.11.2015.
 */

@RunWith(Arquillian.class)
public class SearchServiceIntegrationTest {

    /**
     * class under test
     */
    @EJB
    SearchService searchService;

    @EJB
    AuthenticatedWithRoleUser authenticatedWithRoleUser;
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
                .addClass(FileUtils.class)
                .addClass(AuthenticatedWithRoleUser.class)
                .addClass(AlbumFixture.class)
                .addClass(SearchService.class)
                .addClass(SearchResultEntity.class)
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("mymusictest-ds.xml")
                ;
        System.out.println(webarchive.toString(Formatters.VERBOSE));

        return webarchive;
    }

    /**
     * method under test: findAlbumByName
     */
    @Test
    public void thatFindAlbumByNameReturnCorrectEntries() throws Exception {
        List<Album> fiveAlbums = AlbumFixture.aListOfFiveAlbums();
        authenticatedWithRoleUser.run(() ->
                fiveAlbums.forEach(album ->
                albumService.createOrUpdate(album)
            )
        );

        List<Album> albums = searchService.findAlbumByName("black");

        assertEquals(4, albums.size());
        authenticatedWithRoleUser.run(() ->
                fiveAlbums.forEach(album ->
                        albumService.delete(album)
                )
        );
    }

    /**
     * method under test: findEntitiesByName
     */
    @Test
    public void thatFindEntitiesByNameReturnCorrectElements() throws Exception {
        List<Album> fiveAlbums = AlbumFixture.aListOfFiveAlbums();
        authenticatedWithRoleUser.run(() ->
                fiveAlbums.forEach(album ->
                        albumService.createOrUpdate(album)
                )
        );

        List<SearchResultEntity> result = searchService.findEntitiesByName("black");

        assertEquals(4, result.size());
        authenticatedWithRoleUser.run(() ->
                fiveAlbums.forEach(album ->
                        albumService.delete(album)
                )
        );
    }



}