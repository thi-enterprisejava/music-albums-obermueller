package de.thi.mymusic.service;

import de.thi.mymusic.dao.CrudService;
import de.thi.mymusic.domain.Album;
import de.thi.mymusic.domain.Interpret;
import de.thi.mymusic.domain.Song;
import de.thi.mymusic.fixture.AlbumFixture;
import de.thi.mymusic.fixture.InterpretFixture;
import de.thi.mymusic.security.AuthenticatedWithRoleUser;
import de.thi.mymusic.util.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Michael on 20.12.2015.
 */
public class AlbumServiceTest {

    /**
     * class under test
     */
    AlbumService albumService;

    CrudService mockedCrudService;
    FileUtils mockedFileUtils;
    AuthenticatedWithRoleUser authenticatedWithRoleUser;

    @Before
    public void setUp() throws Exception {
        authenticatedWithRoleUser = new AuthenticatedWithRoleUser();
        albumService = new AlbumService();
        mockedCrudService = mock(CrudService.class);
        mockedFileUtils = mock(FileUtils.class);
        albumService.setCrudService(mockedCrudService);
        albumService.setFileUtils(mockedFileUtils);
    }

    /**
     * method under test: createOrUpdate with save
     */

    @Test
    public void thatCreateOrUpdatePersistNewAlbum() throws Exception {
        when(mockedCrudService.findByNamedQuery(Interpret.class,"Interpret.findByExactName",
                new String[] {"name"}, new Object[] {AlbumFixture.aAlbum().getInterpret().getName()})).thenReturn(null);

        albumService.createOrUpdate(AlbumFixture.aAlbum());

        verify(mockedCrudService).persist(AlbumFixture.aAlbum());
    }

    @Test
    public void thatCreateOrUpdatePersistNewAlbumWithExistingInterpret() throws Exception {
        List<Interpret> interprets = new ArrayList<>();
        interprets.add(InterpretFixture.aInterpret());
        when(mockedCrudService.findByNamedQuery(Interpret.class,"Interpret.findByExactName",
                new String[] {"name"}, new Object[] {AlbumFixture.aAlbum().getInterpret().getName()}))
                .thenReturn(interprets);

        albumService.createOrUpdate(AlbumFixture.aAlbum());

        verify(mockedCrudService).persist(AlbumFixture.aAlbum());
    }


    /**
     * method under test: createOrUpdate with update, deleteOldImageFileIfChanged
     */

    @Test
    public void thatCreateOrUpdateMergeUpdatedAlbumWithNewImage() throws Exception {
        when(mockedCrudService.findByNamedQuery(Interpret.class,"Interpret.findByExactName",
                new String[] {"name"}, new Object[] {AlbumFixture.aAlbumWithId().getInterpret().getName()})).thenReturn(null);
        when(mockedCrudService.findById(Album.class, 2L)).thenReturn(AlbumFixture.aAlbumWithIdOld());
        when(mockedCrudService.findById(Interpret.class, 3L)).thenReturn(InterpretFixture.aInterpretWithTwoAlbums());

        albumService.createOrUpdate(AlbumFixture.aAlbumWithId());

        verify(mockedFileUtils).deleteFile(FileUtils.IMAGE_PATH + File.separator + AlbumFixture.aAlbumWithIdOld().getImageFilename());
        verify(mockedCrudService).merge(AlbumFixture.aAlbumWithId());
    }

    @Test
    public void thatCreateOrUpdateMergeUpdatedAlbum() throws Exception {
        when(mockedCrudService.findByNamedQuery(Interpret.class,"Interpret.findByExactName",
                new String[] {"name"}, new Object[] {AlbumFixture.aAlbumWithId().getInterpret().getName()})).thenReturn(null);
        when(mockedCrudService.findById(Album.class, 2L)).thenReturn(AlbumFixture.aAlbumWithId());
        when(mockedCrudService.findById(Interpret.class, 3L)).thenReturn(InterpretFixture.aInterpretWithTwoAlbums());

        albumService.createOrUpdate(AlbumFixture.aAlbumWithId());

        verify(mockedCrudService).merge(AlbumFixture.aAlbumWithId());
    }

    @Test
    public void thatCreateOrUpdateMergeUpdatedAlbumWithDeletedOrUpdatedSongs() throws Exception {
        when(mockedCrudService.findByNamedQuery(Interpret.class,"Interpret.findByExactName",
                new String[] {"name"}, new Object[] {AlbumFixture.aAlbumWithId().getInterpret().getName()})).thenReturn(null);
        when(mockedCrudService.findById(Album.class, 2L)).thenReturn(AlbumFixture.aAlbumWithIdUpdatedSongs());
        when(mockedCrudService.findById(Interpret.class, 3L)).thenReturn(InterpretFixture.aInterpretWithTwoAlbums());

        albumService.createOrUpdate(AlbumFixture.aAlbumWithId());

        verify(mockedCrudService).delete(any(Song.class));
        verify(mockedCrudService).merge(AlbumFixture.aAlbumWithId());
    }

    @Test
    public void thatCreateOrUpdateMergeUpdatedAlbumWithDeleteInterpretIfChangedAndOnlyOneAlbumWasJoined() throws Exception {
        when(mockedCrudService.findByNamedQuery(Interpret.class,"Interpret.findByExactName",
                new String[] {"name"}, new Object[] {AlbumFixture.aAlbumWithId().getInterpret().getName()})).thenReturn(null);
        when(mockedCrudService.findById(Album.class, 2L)).thenReturn(AlbumFixture.aAlbumWithId());
        when(mockedCrudService.findById(Interpret.class, 3L)).thenReturn(InterpretFixture.aInterpretWithOneAlbums());

        albumService.createOrUpdate(AlbumFixture.aAlbumWithId());

        verify(mockedCrudService).delete(any(Interpret.class));
        verify(mockedCrudService).merge(AlbumFixture.aAlbumWithId());
    }

    /**
     * method under test: delete
     */

    @Test
    public void thatDeleteIsDeletingCorrectAlbumAndNotDeleteInterpretIfAddedToOtherAlbum() throws Exception {
        when(mockedCrudService.findById(Interpret.class, 3L)).thenReturn(InterpretFixture.aInterpretWithTwoAlbums());

        albumService.delete(AlbumFixture.aAlbumWithId());

        verify(mockedFileUtils).deleteFile(FileUtils.IMAGE_PATH + File.separator + AlbumFixture.aAlbumWithId().getImageFilename());
        verify(mockedCrudService).delete(AlbumFixture.aAlbumWithId());
        verify(mockedCrudService, never()).delete(AlbumFixture.aAlbumWithId().getInterpret());
    }

    @Test
    public void thatDeleteIsDeletingCorrectAlbumAndDeleteInterpretIfAddedToOneAlbum() throws Exception {
        when(mockedCrudService.findById(Interpret.class, 3L)).thenReturn(InterpretFixture.aInterpretWithOneAlbums());

        albumService.delete(AlbumFixture.aAlbumWithId());

        verify(mockedFileUtils).deleteFile(FileUtils.IMAGE_PATH + File.separator + AlbumFixture.aAlbumWithId().getImageFilename());
        verify(mockedCrudService).delete(AlbumFixture.aAlbumWithId());
        verify(mockedCrudService).delete(AlbumFixture.aAlbumWithId().getInterpret());
    }

    /**
     * method under test: findById
     */

    @Test
    public void thatFindByIdReturnCorrectAlbum() throws Exception {
        when(mockedCrudService.findById(Album.class, 3L)).thenReturn(AlbumFixture.aAlbumWithId());

        Album foundedAlbum = albumService.findById(3L);

        verify(mockedCrudService).findById(Album.class, 3L);
        assertEquals(AlbumFixture.aAlbumWithId(), foundedAlbum);
    }

    @Test
    public void thatFindByIdReturnNullIfNoAlbumWasFound() throws Exception {
        when(mockedCrudService.findById(Album.class, 4L)).thenReturn(null);

        Album foundedAlbum = albumService.findById(4L);

        verify(mockedCrudService).findById(Album.class, 4L);
        assertNull(foundedAlbum);
    }

    /**
     * method under test: findAll
     */

    @Test
    public void thatFindAllReturnAlbumList() throws Exception {
        when(mockedCrudService.findAll(Album.class)).thenReturn(Arrays.asList(
                AlbumFixture.aAlbumWithId(), AlbumFixture.aAlbumWithIdOld()
        ));

        List<Album> foundedAlbums = albumService.findAll();

        assertEquals(2, foundedAlbums.size());
    }

}