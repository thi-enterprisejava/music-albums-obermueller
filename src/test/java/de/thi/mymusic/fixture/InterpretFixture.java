package de.thi.mymusic.fixture;

import de.thi.mymusic.domain.Album;
import de.thi.mymusic.domain.Interpret;
import de.thi.mymusic.domain.Song;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Michael on 17.12.2015.
 */
public class InterpretFixture {

    public static Interpret aInterpret() {
        return new Interpret("Beatsteaks");
    }


    public static Interpret aInterpretWithOneAlbums() {
        Interpret interpret = new Interpret("Beatsteaks");
        Album albumLimboMessiah = new Album("Limbo messiah", interpret,
                Arrays.asList(new Song(1, "As I please", "03:12"),
                new Song(2, "Meantime", "02:14")), 2010);
        List<Album> albums = new ArrayList<>();
        albums.add(albumLimboMessiah);
        interpret.setAlbums(albums);
        return interpret;
    }

    public static Interpret aInterpretWithTwoAlbums() {
        Interpret interpret = new Interpret("Beatsteaks");
        Album albumLimboMessiah = new Album("Limbo messiah", interpret,
                Arrays.asList(new Song(1, "As I please", "03:12"),
                new Song(2, "Meantime", "02:14")), 2010);
        Album albumBeatsteaks = new Album("Beatsteaks", interpret,
                Arrays.asList(new Song(1, "As I please", "03:12"),
                new Song(2, "Meantime", "02:14")) , 2014);
        List<Album> albums = new ArrayList<>();
        albums.add(albumLimboMessiah);
        albums.add(albumBeatsteaks);
        interpret.setAlbums(albums);
        return interpret;
    }

    public static List<Interpret> aListOfTwoInterprets() {
        return Arrays.asList(new Interpret("Messiahs"), new Interpret("Mess band"));
    }

}
