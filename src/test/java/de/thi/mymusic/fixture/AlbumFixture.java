package de.thi.mymusic.fixture;

import de.thi.mymusic.domain.Album;
import de.thi.mymusic.domain.Interpret;
import de.thi.mymusic.domain.Song;

import java.util.Arrays;
import java.util.List;

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

    public static Album aAlbumWithId() {
        Interpret interpret = new Interpret("Beatsteaks");
        interpret.setId(3);
        Album album =  new Album("Limbo messiah", interpret,
                Arrays.asList(new Song(1, "As I please", "03:12"),
                        new Song(2, "Meantime", "02:14")), 2007);
        album.setId(2);
        album.setImageFilename("bildname.jpg");

        return album;
    }

    public static Album aAlbumWithIdUpdatedSongs() {
        Interpret interpret = new Interpret("Beatsteaks");
        interpret.setId(3);
        Album album =  new Album("Limbo messiah", interpret,
                Arrays.asList(new Song(1, "As I please Updated", "03:12")), 2007);
        album.setId(2);
        album.setImageFilename("bildname.jpg");

        return album;
    }

    public static Album aAlbumWithIdOld() {
        Interpret interpret = new Interpret("Beatsteaks");
        interpret.setId(3);
        Album album =  new Album("Limbo messiah", interpret,
                Arrays.asList(new Song(1, "As I please", "03:12"),
                        new Song(2, "Meantime", "02:14")), 2007);
        album.setId(2);
        album.setImageFilename("bildnameOld.jpg");

        return album;
    }

    public static Album aAlbumWithoutInterpret() {
        Album album =  new Album("Limbo messiah", null,
                Arrays.asList(new Song(1, "As I please", "03:12"),
                        new Song(2, "Meantime", "02:14")), 2007);

        return album;
    }

    public static List<Album> aListOfFiveAlbums() {
        return Arrays.asList(new Album("Black Messiah", new Interpret("D'Angelo"), Arrays.asList(new Song(1, "Ain´t That Easy", "04:49"),
                new Song(2, "1000 Deaths", "05:50")), 2014),
                new Album("Black Messiah2", new Interpret("D'Angelo"), Arrays.asList(new Song(1, "Ain´t That Easy", "04:49"),
                        new Song(2, "1000 Deaths", "05:50")), 2014),
                new Album("Black Messiah3", new Interpret("D'Angelo"), Arrays.asList(new Song(1, "Ain´t That Easy", "04:49"),
                        new Song(2, "1000 Deaths", "05:50")), 2014),
                new Album("Black Messiah4", new Interpret("D'Angelo"), Arrays.asList(new Song(1, "Ain´t That Easy", "04:49"),
                        new Song(2, "1000 Deaths", "05:50")), 2014),
                new Album("Limbo messiah", new Interpret("Beatsteaks"), Arrays.asList(new Song(1, "As I please", "03:12"),
                        new Song(2, "Meantime", "02:14")), 2007));
    }
}
