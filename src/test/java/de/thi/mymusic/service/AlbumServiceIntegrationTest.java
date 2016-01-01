package de.thi.mymusic.service;

import de.thi.mymusic.dao.CrudService;
import de.thi.mymusic.domain.*;
import de.thi.mymusic.fixture.AlbumFixture;
import de.thi.mymusic.fixture.InterpretFixture;
import de.thi.mymusic.fixture.SongFixture;
import de.thi.mymusic.fixture.UserFixture;
import de.thi.mymusic.security.AuthenticatedWithRoleAdmin;
import de.thi.mymusic.security.AuthenticatedWithRoleUser;
import de.thi.mymusic.util.FileUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.ejb.EJBAccessException;

import static org.junit.Assert.*;

/**
 * Created by Michael on 20.11.2015.
 */

@RunWith(Arquillian.class)
public class AlbumServiceIntegrationTest {

    /**
     * class under test
     */
    @EJB
    AlbumService albumService;

    @EJB
    AuthenticatedWithRoleUser authenticatedWithRoleUser;

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
                .addClass(AuthenticatedWithRoleAdmin.class)
                .addClass(AuthenticatedWithRoleUser.class)
                .addClass(AlbumFixture.class)
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("mymusictest-ds.xml")
                ;
        System.out.println(webarchive.toString(Formatters.VERBOSE));

        return webarchive;
    }

    /**
     * method under test: createOrUpdate with save
     */

    @Test
    public void thatAlbumCanBeAddedWithUserRole() throws Exception {
        authenticatedWithRoleUser.run(() -> {
            Album album = AlbumFixture.aAlbum();

            albumService.createOrUpdate(album);

            Album savedAlbum = albumService.findById(album.getId());
            assertEquals(AlbumFixture.aAlbum().getTitle(), savedAlbum.getTitle());
            assertEquals(AlbumFixture.aAlbum().getSongs().size(), savedAlbum.getSongs().size());
            assertEquals(AlbumFixture.aAlbum().getInterpret().getName(), savedAlbum.getInterpret().getName());
            assertNotEquals(0, savedAlbum.getId());
            assertNotEquals(0, savedAlbum.getInterpret().getId());
            assertNotEquals(0, savedAlbum.getId());
            albumService.delete(savedAlbum);
        });
    }

    @Test (expected = EJBAccessException.class)
    public void thatAlbumCanNotBeAddedAnonymous() throws Exception {
        Album album = AlbumFixture.aAlbum();

        albumService.createOrUpdate(album);
    }

    /**
     * method under test: findById
     */

    @Test
    public void thatFindByIdReturnAlbum() throws Exception {
        Album album = AlbumFixture.aAlbum();
        authenticatedWithRoleUser.run(() -> albumService.createOrUpdate(album));

        Album foundedAlbum = albumService.findById(album.getId());

        assertNotNull(foundedAlbum);
        assertEquals(album, foundedAlbum);
        authenticatedWithRoleUser.run(() -> albumService.delete(album));
    }

    /**
     * method under test: delete
     */

    @Test
    public void thatDeleteIsPossibleAsRoleUser() throws Exception {
        Album album = AlbumFixture.aAlbum();
        authenticatedWithRoleUser.run(() -> albumService.createOrUpdate(album));
        long id = album.getId();

        authenticatedWithRoleUser.run(() -> albumService.delete(album));

        assertNull(albumService.findById(id));
    }

    @Test
    public void thatDeleteIsNotPossibleAnonymous() throws Exception {
        Album album = AlbumFixture.aAlbum();
        authenticatedWithRoleUser.run(() -> albumService.createOrUpdate(album));
        try {

            albumService.delete(album);

            fail("Should throw an EJBAccessException ");
        } catch (EJBAccessException ex) {

        } finally {
            authenticatedWithRoleUser.run(() -> albumService.delete(album));
        }
    }

}