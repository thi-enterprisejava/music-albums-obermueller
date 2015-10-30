package de.thi.mymusic.web.model;

import de.thi.mymusic.domain.Song;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Michael on 30.10.2015.
 */
public class SelectedAlbumTest {

    SelectedAlbum selectedAlbum;

    @Before
    public void setUp() throws Exception {
        selectedAlbum = new SelectedAlbum();
        selectedAlbum.setNewSongTitle("Test");
        selectedAlbum.setNewSongDuration("03:17");
    }

    @Test
    public void testDoSave() throws Exception {


    }

    @Test
    public void testDoAddSong() throws Exception {
        selectedAlbum.doAddSong();
        List<Song> songs= selectedAlbum.getAlbum().getSongs();
        assertNotNull(songs);
    }
}