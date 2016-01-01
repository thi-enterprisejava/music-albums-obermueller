package de.thi.mymusic.domain;

import de.thi.mymusic.fixture.AlbumFixture;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Michael on 01.01.2016.
 */
public class AlbumTest {

    /**
     * class under test
     */
    Album album;

    /**
     * method under test: formattedTotalDuration
     */

    @Test
    public void thatFormattedTotalDurationReturnTotalDurationCorrect() throws Exception {
        album = AlbumFixture.aAlbum();

        String formattedDuration = album.formattedTotalDuration();

        assertEquals("05:26", formattedDuration);
    }
}