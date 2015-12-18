package de.thi.mymusic.fixture;

import de.thi.mymusic.domain.Album;
import de.thi.mymusic.domain.Interpret;
import de.thi.mymusic.domain.Song;

import java.util.Arrays;

/**
 * Created by Michael on 17.12.2015.
 */
public class AlbumFixture {

    public static Album aAlbum() {
        Album album =  new Album("Limbo messiah", new Interpret("Beatsteaks"),
                Arrays.asList(new Song(1, "As I please", "03:12"),
                new Song(2, "Meantime", "02:14")), 2007);

        return album;
    }

    public static Album aAlbumWithoutInterpret() {
        Album album =  new Album("Limbo messiah", null,
                Arrays.asList(new Song(1, "As I please", "03:12"),
                        new Song(2, "Meantime", "02:14")), 2007);

        return album;
    }
}
