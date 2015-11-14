package de.thi.mymusic.web.model;

import de.thi.mymusic.domain.Album;
import de.thi.mymusic.domain.Song;
import de.thi.mymusic.repository.AlbumRepositoryImpl;
import de.thi.mymusic.repository.Repository;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * JUnit Test for model class: SelectedAlbum
 *
 * Created by Michael on 30.10.2015.
 */

//TODO Implement JUnit Test for SelectedAlbum Model
public class SelectedAlbumTest {

    /**
     * Class under test
     */
    SelectedAlbum selectedAlbum;

    Repository<Album> mockedAlbumRepository;

    //@Before
    /*public void setUp() throws Exception {
        mockedAlbumRepository = mock(AlbumRepositoryImpl.class);
        selectedAlbum = new SelectedAlbum(mockedAlbumRepository);
    }*/

    //@Test
    public void thatSaveAlbumAddsNewAlbum() throws Exception {


    }

    //@Test
    public void thatAddSongCreateNewSongInAlbum() throws Exception {

        selectedAlbum.doAddSong();

        List<Song> songs= selectedAlbum.getAlbum().getSongs();
        assertNotNull(songs);
    }
}