package de.thi.mymusic.fixture;

import de.thi.mymusic.domain.Song;

/**
 * Created by Michael on 17.12.2015.
 */
public class SongFixture {

    public static Song aSong(){
        return new Song(1, "As I please", "03:12");
    }
}
