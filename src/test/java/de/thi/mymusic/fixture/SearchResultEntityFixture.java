package de.thi.mymusic.fixture;

import de.thi.mymusic.domain.SearchResultEntity;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Michael on 30.12.2015.
 */
public class SearchResultEntityFixture {

    public static List<SearchResultEntity> aListOfAlbumSearchResultEntities() {
        return Arrays.asList(new SearchResultEntity("Limbo messiah", "Album"),
                new SearchResultEntity("Black Messiah", "Album"),
                new SearchResultEntity("Messia", "Album"));
    }

    public static List<SearchResultEntity> aListOfInterpretSearchResultEntities() {
        return Arrays.asList(new SearchResultEntity("Limbo messiah", "Interpret"),
                new SearchResultEntity("Black Messiah", "Interpret"),
                new SearchResultEntity("Messia", "Interpret"));
    }

    public static List<SearchResultEntity> aListOfSongSearchResultEntities() {
        return Arrays.asList(new SearchResultEntity("Limbo messiah", "Song"),
                new SearchResultEntity("Black Messiah", "Song"),
                new SearchResultEntity("Messia", "Song"));
    }
}
