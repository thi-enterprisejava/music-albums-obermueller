package de.thi.mymusic.web.model;

import de.thi.mymusic.domain.Interpret;
import de.thi.mymusic.domain.Song;
import de.thi.mymusic.domain.Album;
import de.thi.mymusic.fixture.AlbumFixture;
import de.thi.mymusic.fixture.InterpretFixture;
import de.thi.mymusic.fixture.SongFixture;
import de.thi.mymusic.service.AlbumService;
import de.thi.mymusic.util.FileUtils;
import de.thi.mymusic.util.GuiUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import java.io.File;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ FacesContext.class, GuiUtils.class, FileUtils.class, UUID.class})
public class SelectedAlbumTest {

    /**
     * Class under test
     */
    SelectedAlbum selectedAlbum;

    AlbumService mockedAlbumService;
    @Mock
    FacesContext mockedFacesContext;
    @Mock
    GuiUtils mockedGuiUtils;
    @Mock
    FileUtils mockedFileUtils;
    @Mock
    UUID mockedUUID;

    @Before
    public void setUp() throws Exception {
        mockedAlbumService = mock(AlbumService.class);
        selectedAlbum = new SelectedAlbum(mockedAlbumService);
        PowerMockito.mockStatic(FacesContext.class);
    }

    /**
     * method under test: init
     */

    @Test
    public void thatInitLoadCorrectAlbum() throws Exception {
        selectedAlbum.setAlbumId(1);
        when(mockedAlbumService.findById(1)).thenReturn(AlbumFixture.aAlbum());

        String viewResult = selectedAlbum.init();

        assertNotNull(selectedAlbum.getAlbum());
        assertEquals(AlbumFixture.aAlbum().getInterpret(), selectedAlbum.getInterpret());
        assertEquals(AlbumFixture.aAlbum().getImageFilename(), selectedAlbum.getImageName());
        assertEquals(3, selectedAlbum.getCurrentSongNumber());
        assertNull(viewResult);
    }

    @Test
    public void thatInitReturnErrorPageIfAlbumWasNotFound() throws Exception {
        selectedAlbum.setAlbumId(2L);
        when(mockedAlbumService.findById(2L)).thenReturn(null);
        when(FacesContext.getCurrentInstance()).thenReturn(mockedFacesContext);
        PowerMockito.mockStatic(GuiUtils.class);

        String viewResult = selectedAlbum.init();

        Album album = selectedAlbum.getAlbum();
        assertNull(album);
        assertNotNull(viewResult);
        assertEquals("error", viewResult);
    }

    @Test
    public void thatInitReturnNullIfAlbumIdIs0() throws Exception {
        selectedAlbum.setAlbumId(0L);

        String viewResult = selectedAlbum.init();

        assertNull(viewResult);
    }

    /**
     * method under test: doSave
     */

    @Test
    @Ignore
    //TODO Test doSave with FileUpload
    public void thatDoSaveAddNewAlbumWithImage() throws Exception {
        PowerMockito.mockStatic(FileUtils.class);
        PowerMockito.mockStatic(UUID.class);
        //Part imageFile = new Part();
        //when(FileUtils.getFileNameFromPart()).thenReturn()
        selectedAlbum.setInterpret(InterpretFixture.aInterpret());
        selectedAlbum.setAlbum(AlbumFixture.aAlbumWithoutInterpret());

        selectedAlbum.doSave();

        assertEquals(InterpretFixture.aInterpret().getName(), selectedAlbum.getInterpret().getName());
        verify(mockedAlbumService).createOrUpdate(AlbumFixture.aAlbum());

    }

    @Test
    @Ignore
    public void thatAddSongCreateNewSongInAlbum() throws Exception {

        selectedAlbum.doAddSong();

        List<Song> songs = selectedAlbum.getAlbum().getSongs();
        assertNotNull(songs);
    }

    /**
     * method under test: doAddSong
     */

    @Test
    public void ThatDoAddSongAddNewSongCorrect() throws Exception {
        selectedAlbum.setCurrentSongNumber(1);
        selectedAlbum.setNewSongTitle(SongFixture.aSong().getTitle());
        selectedAlbum.setNewSongDuration(SongFixture.aSong().getFormattedDuration());

        String viewResult = selectedAlbum.doAddSong();

        assertNotNull(selectedAlbum.getAlbum().getSongs().get(0));
        assertEquals(SongFixture.aSong().getDuration(), selectedAlbum.getAlbum().getSongs().get(0).getDuration());
        assertEquals(SongFixture.aSong().getTitle(), selectedAlbum.getAlbum().getSongs().get(0).getTitle());
        assertNull(selectedAlbum.getNewSongDuration());
        assertNull(selectedAlbum.getNewSongTitle());
        assertEquals(2, selectedAlbum.getCurrentSongNumber());
        assertNull(viewResult);
    }

    @Test
    public void ThatDoAddSongEditSongCorrect() throws Exception {
        selectedAlbum.setCurrentSongNumber(1);
        selectedAlbum.setNewSongTitle(SongFixture.aSong().getTitle());
        selectedAlbum.setNewSongDuration(SongFixture.aSong().getFormattedDuration());
        selectedAlbum.doAddSong();
        selectedAlbum.doPrepareEditSong(selectedAlbum.getAlbum().getSongs().get(0));
        selectedAlbum.setNewSongTitle("Meantime");
        selectedAlbum.setNewSongDuration("02:16");

        String viewResult = selectedAlbum.doAddSong();

        assertNotNull(selectedAlbum.getAlbum().getSongs().get(0));
        assertEquals("02:16", selectedAlbum.getAlbum().getSongs().get(0).getFormattedDuration());
        assertEquals("Meantime", selectedAlbum.getAlbum().getSongs().get(0).getTitle());
        assertNull(selectedAlbum.getNewSongDuration());
        assertNull(selectedAlbum.getNewSongTitle());
        assertEquals(2, selectedAlbum.getCurrentSongNumber());
        assertNull(viewResult);
    }

    /**
     * method under test doPrepareForEditSong
     */

    @Test
    public void thatDoPrepareEditSongLoadCorrectSong() throws Exception {
        selectedAlbum.setCurrentSongNumber(1);
        selectedAlbum.setNewSongTitle(SongFixture.aSong().getTitle());
        selectedAlbum.setNewSongDuration(SongFixture.aSong().getFormattedDuration());
        selectedAlbum.doAddSong();

        String viewResult = selectedAlbum.doPrepareEditSong(selectedAlbum.getAlbum().getSongs().get(0));

        assertEquals(1, selectedAlbum.getCurrentSongNumber());
        assertEquals(SongFixture.aSong().getFormattedDuration(), selectedAlbum.getNewSongDuration());
        assertEquals(SongFixture.aSong().getTitle(), selectedAlbum.getNewSongTitle());
        assertNull(viewResult);
    }

    /**
     * method under test: doDeleteSong
     */

    @Test
    public void ThatDoDeleteSongRemoveSongFromAlbum() throws Exception {
        selectedAlbum.setCurrentSongNumber(1);
        selectedAlbum.setNewSongTitle(SongFixture.aSong().getTitle());
        selectedAlbum.setNewSongDuration(SongFixture.aSong().getFormattedDuration());
        selectedAlbum.doAddSong();

        String viewResult = selectedAlbum.doDeleteSong(selectedAlbum.getAlbum().getSongs().get(0));

        assertEquals(0, selectedAlbum.getAlbum().getSongs().size());
        assertNull(viewResult);
    }

    /**
     * method under test: doCancel
     */

    @Test
    public void thatDoCancelResetAllByCreate() throws Exception {
        selectedAlbum.setAlbumId(0L);
        selectedAlbum.setCurrentSongNumber(3);
        selectedAlbum.setNewSongTitle(SongFixture.aSong().getTitle());
        selectedAlbum.setNewSongDuration(SongFixture.aSong().getFormattedDuration());
        selectedAlbum.setAlbum(AlbumFixture.aAlbumWithoutInterpret());
        selectedAlbum.setInterpret(InterpretFixture.aInterpret());

        String viewResult = selectedAlbum.doCancel();

        assertEquals("edit.xhtml", viewResult);
        assertEquals(new Interpret(), selectedAlbum.getInterpret());
        assertEquals(new Album(), selectedAlbum.getAlbum());
        assertNull(selectedAlbum.getNewSongDuration());
        assertEquals(1, selectedAlbum.getCurrentSongNumber());
        assertNull(selectedAlbum.getNewSongTitle());
    }

    @Test
    public void thatDoCancelRedirectByEditAlbum() throws Exception {
        selectedAlbum.setAlbumId(1L);

        String viewResult = selectedAlbum.doCancel();

        assertEquals("edit.xhtml?album=1&faces-redirect=true", viewResult);
    }

    //TODO Test for method doDelete

    /**
     * method under test: doDeleteImage
     */

    @Test
    public void thatDoDeleteImageSetImageNameToNull() throws Exception {
        selectedAlbum.setImageName("album_cover.jpg");

        String viewResult = selectedAlbum.doDeleteImage();

        assertNull(viewResult);
        assertNull(selectedAlbum.getImageName());
    }

}