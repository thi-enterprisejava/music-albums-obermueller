package de.thi.mymusic.fixture;

import de.thi.mymusic.domain.Song;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Michael on 17.12.2015.
 */
public class SongFixture {

    public static Song aSong(){
        return new Song(1, "As I please", "03:12");
    }

    public static List<Song> aListOfTwoSongs() {
        return Arrays.asList(new Song(1, "Black Messiah", "03:12"), new Song(2, "Messiah", "03:12"));
    }
}
