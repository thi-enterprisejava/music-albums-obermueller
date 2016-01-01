package de.thi.mymusic.web.model;

import de.thi.mymusic.domain.Album;
import de.thi.mymusic.domain.Interpret;
import de.thi.mymusic.domain.SearchResultEntity;
import de.thi.mymusic.domain.Song;
import de.thi.mymusic.fixture.AlbumFixture;
import de.thi.mymusic.fixture.InterpretFixture;
import de.thi.mymusic.fixture.SearchResultEntityFixture;
import de.thi.mymusic.fixture.SongFixture;
import de.thi.mymusic.service.SearchService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * JUnit-Test for model class: Search
 *
 * Created by Michael on 06.11.2015.
 */

public class SearchTest {

    /**
     * class under test
     */
    Search search;

    SearchService mockedSearchService;

    @Before
    public void setUp() throws Exception {
        mockedSearchService = mock(SearchService.class);
        search = new Search(mockedSearchService);
    }

    /**
     * method under test doSearch
     */

    @Test
    public void thatDoSearchFindOneEntryCorrect() throws Exception {
        when(mockedSearchService.findAlbumByName("Limbo messiah")).
                thenReturn(Arrays.asList(AlbumFixture.aAlbumWithId()));
        search.setSearchString("Limbo messiah");

        String resultString = search.doSearch();

        assertEquals(1, search.getAlbumResult().size());
        assertEquals(Arrays.asList(AlbumFixture.aAlbumWithId()), search.getAlbumResult());
        assertEquals(1, search.getCountAlbumResult());
        assertEquals(0,search.getInterpretResult().size());
        assertEquals(0, search.getCountInterpretResult());
        assertEquals(0, search.getSongResult().size());
        assertEquals(0, search.getCountSongResult());
        assertEquals(AlbumFixture.aAlbumWithId(), search.getAlbumResult().get(0));
        assertEquals("listSearchResult", resultString);
    }

    @Test
    public void thatDoSearchFindMoreEntriesCorrect() throws Exception {
        when(mockedSearchService.findAlbumByName("mess")).
                thenReturn(AlbumFixture.aListOfFiveAlbums());
        when(mockedSearchService.findInterpretByName("mess")).thenReturn(InterpretFixture.aListOfTwoInterprets());
        when(mockedSearchService.findSongByName("mess")).thenReturn(SongFixture.aListOfTwoSongs());
        search.setSearchString("mess");

        String resultString = search.doSearch();

        assertEquals(5, search.getAlbumResult().size());
        assertEquals(AlbumFixture.aListOfFiveAlbums(), search.getAlbumResult());
        assertEquals(5, search.getCountAlbumResult());
        assertEquals(2,search.getInterpretResult().size());
        assertEquals(InterpretFixture.aListOfTwoInterprets(),search.getInterpretResult());
        assertEquals(2, search.getCountInterpretResult());
        assertEquals(2, search.getSongResult().size());
        assertEquals(SongFixture.aListOfTwoSongs(), search.getSongResult());
        assertEquals(2, search.getCountSongResult());
        assertEquals("listSearchResult", resultString);
    }


    /**
     * method under test completeSearchInput
     */

    @Test
    public void thatCompleteSearchInputFindCorrectEntries() throws Exception{
        when(mockedSearchService.findEntitiesByName("mess")).thenReturn(SearchResultEntityFixture.aListOfAlbumSearchResultEntities());

        List<SearchResultEntity> searchResult = search.completeSearchInput("mess");

        assertNotNull(searchResult);
        assertEquals(SearchResultEntityFixture.aListOfAlbumSearchResultEntities().size(), searchResult.size());
        assertEquals(SearchResultEntityFixture.aListOfAlbumSearchResultEntities(), searchResult);
    }

}