package de.thi.mymusic.web.model;

import de.thi.mymusic.domain.Album;
import de.thi.mymusic.domain.Interpret;
import de.thi.mymusic.domain.Song;
import de.thi.mymusic.repository.Repository;
import de.thi.mymusic.service.AlbumService;
import org.junit.After;
import org.junit.Before;
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
     * Search
     */
    Search search;

    AlbumService mockedAlbumService;

    @Before
    public void setUp() throws Exception {
        mockedAlbumService = mock(AlbumService.class);
        search = new Search(mockedAlbumService);
    }


    @Test
    public void thatSearchFindOneEntryCorrect() throws Exception {
        when(mockedAlbumService.findByName("Limbo messiah")).
                thenReturn(Arrays.asList(new Album("Limbo messiah", new Interpret("Beatsteaks"), Arrays.asList(new Song(1, "As I please", "03:12"),
                        new Song(2, "Meantime", "02:14")), 2007)));
        search.setSearchString("Limbo messiah");

        String resultString = search.doSearch();

        assertEquals(1, search.getResult().size());
        assertEquals(new Album("Limbo messiah", new Interpret("Beatsteaks"), Arrays.asList(new Song(1, "As I please", "03:12"),
                new Song(2, "Meantime", "02:14")), 2007), search.getResult().get(0));
        assertEquals("detailAlbum.xhtml?faces-redirect=true&album=0", resultString);
    }

    @Test
    public void thatSearchFindMoreEntriesCorrect() throws Exception {
        when(mockedAlbumService.findByName("messiah")).
                thenReturn(Arrays.asList(new Album("Black Messiah", new Interpret("D'Angelo"), Arrays.asList(new Song(1, "Ain´t That Easy", "04:49"),
                        new Song(2, "1000 Deaths", "05:50")), 2014),
                        new Album("Limbo messiah", new Interpret("Beatsteaks"), Arrays.asList(new Song(1, "As I please", "03:12"),
                                new Song(2, "Meantime", "02:14")), 2007)));
        search.setSearchString("messiah");

        String resultString = search.doSearch();

        assertEquals(2, search.getResult().size());
        assertEquals(search.getResult(), Arrays.asList(new Album("Black Messiah", new Interpret("D'Angelo"), Arrays.asList(new Song(1, "Ain´t That Easy", "04:49"),
                        new Song(2, "1000 Deaths", "05:50")), 2014),
                new Album("Limbo messiah", new Interpret("Beatsteaks"), Arrays.asList(new Song(1, "As I please", "03:12"),
                        new Song(2, "Meantime", "02:14")), 2007)));
        assertEquals("listSearchResult", resultString);
    }

    //TODO Test if no entry was found


    @Test
    public void thatAutoCompleteFindCorrectEntries() throws Exception{
        when(mockedAlbumService.findByName("messiah")).
                thenReturn(Arrays.asList(new Album("Black Messiah", new Interpret("D'Angelo"), Arrays.asList(new Song(1, "Ain´t That Easy", "04:49"),
                        new Song(2, "1000 Deaths", "05:50")), 2014),
                        new Album("Limbo messiah", new Interpret("Beatsteaks"), Arrays.asList(new Song(1, "As I please", "03:12"),
                                new Song(2, "Meantime", "02:14")), 2007)));

        List<String> searchResult = search.completeSearchInput("messiah");

        assertEquals(2, searchResult.size());
        assertEquals(searchResult, Arrays.asList("Black Messiah", "Limbo messiah"));
    }

    @Test
    public void thatAutoCompleteReturnsMaxFiveElements() throws Exception {
        when(mockedAlbumService.findByName("messiah")).
                thenReturn(Arrays.asList(new Album("Black Messiah", new Interpret("D'Angelo"), Arrays.asList(new Song(1, "Ain´t That Easy", "04:49"),
                        new Song(2, "1000 Deaths", "05:50")), 2014),
                        new Album("Black Messiah2", new Interpret("D'Angelo"), Arrays.asList(new Song(1, "Ain´t That Easy", "04:49"),
                                new Song(2, "1000 Deaths", "05:50")), 2014),
                        new Album("Black Messiah3", new Interpret("D'Angelo"), Arrays.asList(new Song(1, "Ain´t That Easy", "04:49"),
                                new Song(2, "1000 Deaths", "05:50")), 2014),
                        new Album("Black Messiah4", new Interpret("D'Angelo"), Arrays.asList(new Song(1, "Ain´t That Easy", "04:49"),
                                new Song(2, "1000 Deaths", "05:50")), 2014),
                        new Album("Black Messiah5", new Interpret("D'Angelo"), Arrays.asList(new Song(1, "Ain´t That Easy", "04:49"),
                                new Song(2, "1000 Deaths", "05:50")), 2014),
                        new Album("Black Messiah6", new Interpret("D'Angelo"), Arrays.asList(new Song(1, "Ain´t That Easy", "04:49"),
                                new Song(2, "1000 Deaths", "05:50")), 2014),
                        new Album("Limbo messiah", new Interpret("Beatsteaks"), Arrays.asList(new Song(1, "As I please", "03:12"),
                                new Song(2, "Meantime", "02:14")), 2007)));

        List<String> searchResult = search.completeSearchInput("messiah");

        assertEquals(5, searchResult.size());
    }

    @Test
    public void thatAutoCompleteReturnEmptyListIfNothingFounded() throws Exception{
        when(mockedAlbumService.findByName("Beat")).
                thenReturn(Arrays.asList());

        List<String> searchResult = search.completeSearchInput("Beat");

        assertEquals(0, searchResult.size());
    }
}