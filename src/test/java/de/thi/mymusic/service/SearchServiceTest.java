package de.thi.mymusic.service;

import de.thi.mymusic.dao.CrudService;
import de.thi.mymusic.domain.Album;
import de.thi.mymusic.domain.Interpret;
import de.thi.mymusic.domain.SearchResultEntity;
import de.thi.mymusic.domain.Song;
import de.thi.mymusic.fixture.AlbumFixture;
import de.thi.mymusic.fixture.InterpretFixture;
import de.thi.mymusic.fixture.SearchResultEntityFixture;
import de.thi.mymusic.fixture.SongFixture;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Michael on 29.12.2015.
 */
public class SearchServiceTest {

    /**
     * class under test
     */
    SearchService searchService;

    CrudService mockedCrudService;
    EntityManager mockedEntityManager;

    @Before
    public void setUp() throws Exception {
        searchService = new SearchService();
        mockedCrudService = mock(CrudService.class);
        searchService.setCrudService(mockedCrudService);
    }


    /**
     * method under test: findAlbumByName
     */

    @Test
    public void thatFindAlbumByNameReturnSearchedAlbums() throws Exception {
        when(mockedCrudService.findByNamedQuery(Album.class,"Album.findByName",
                new String[] {"name"}, new Object[] {"mess"})).thenReturn(AlbumFixture.aListOfFiveAlbums());

        List<Album> foundedAlbums = searchService.findAlbumByName("mess");

        assertNotNull(foundedAlbums);
        assertEquals(5, foundedAlbums.size());
    }

    @Test
    public void thatFindAlbumByNameReturnNullIfNoAlbumWasFound() throws Exception {
        when(mockedCrudService.findByNamedQuery(Album.class,"Album.findByName",
                new String[] {"name"}, new Object[] {"mess"})).thenReturn(null);

        List<Album> foundedAlbums = searchService.findAlbumByName("mess");

        assertNull(foundedAlbums);
    }

    /**
     * method under test: findInterpretByName
     */

    @Test
    public void thatFindInterpretByNameReturnSearchedInterprets() throws Exception {
        when(mockedCrudService.findByNamedQuery(Interpret.class,"Interpret.findByName",
                new String[] {"name"}, new Object[] {"mess"})).thenReturn(InterpretFixture.aListOfTwoInterprets());

        List<Interpret> foundedInterprets = searchService.findInterpretByName("mess");

        assertNotNull(foundedInterprets);
        assertEquals(2, foundedInterprets.size());
    }

    @Test
    public void thatFindInterpretByNameReturnNullIfNoInterpretWasFound() throws Exception {
        when(mockedCrudService.findByNamedQuery(Interpret.class,"Interpret.findByName",
                new String[] {"name"}, new Object[] {"mess"})).thenReturn(null);

        List<Interpret> foundedInterprets = searchService.findInterpretByName("mess");

        assertNull(foundedInterprets);
    }

    /**
     * method under test: findSongByName
     */

    @Test
    public void thatFindSongByNameReturnSearchedSongs() throws Exception {
        when(mockedCrudService.findByNamedQuery(Song.class,"Song.findByName",
                new String[] {"name"}, new Object[] {"mess"})).thenReturn(SongFixture.aListOfTwoSongs());

        List<Song> foundedSongs = searchService.findSongByName("mess");

        assertNotNull(foundedSongs);
        assertEquals(2, foundedSongs.size());
    }

    @Test
    public void thatFindSongsByNameReturnNullIfNoSongWasFound() throws Exception {
        when(mockedCrudService.findByNamedQuery(Song.class,"Song.findByName",
                new String[] {"name"}, new Object[] {"mess"})).thenReturn(null);

        List<Song> foundedSongs = searchService.findSongByName("mess");

        assertNull(foundedSongs);
    }

    /**
     * method under test: findEntitiesByName
     */

    @Test
    public void thatFindEntitiesByNameReturnMaxFiveSearchResultEntities() throws Exception {
        mockedEntityManager = mock(EntityManager.class);
        TypedQuery<SearchResultEntity> mockedTypedQueryAlbum = mock(TypedQuery.class);
        TypedQuery<SearchResultEntity> mockedTypedQueryInterpret = mock(TypedQuery.class);
        TypedQuery<SearchResultEntity> mockedTypedQuerySong = mock(TypedQuery.class);
        when(mockedCrudService.getEntityManager()).thenReturn(mockedEntityManager);
        when(mockedEntityManager.createQuery("SELECT NEW de.thi.mymusic.domain.SearchResultEntity(a.title, 'Album') FROM Album as a WHERE UPPER(a.title) like CONCAT('%', Upper(:name), '%')",
                SearchResultEntity.class)).thenReturn(mockedTypedQueryAlbum);
        when(mockedEntityManager.createQuery("SELECT NEW de.thi.mymusic.domain.SearchResultEntity(i.name, 'Interpret') FROM Interpret as i WHERE UPPER(i.name) like CONCAT('%', Upper(:name), '%')",
                SearchResultEntity.class)).thenReturn(mockedTypedQueryInterpret);
        when(mockedEntityManager.createQuery("SELECT NEW de.thi.mymusic.domain.SearchResultEntity(s.title, 'Song') FROM Song as s WHERE UPPER(s.title) like CONCAT('%', Upper(:name), '%')",
                SearchResultEntity.class)).thenReturn(mockedTypedQuerySong);
        when(mockedTypedQueryAlbum.getResultList()).thenReturn(SearchResultEntityFixture.aListOfAlbumSearchResultEntities());
        when(mockedTypedQueryInterpret.getResultList()).thenReturn(SearchResultEntityFixture.aListOfInterpretSearchResultEntities());
        when(mockedTypedQuerySong.getResultList()).thenReturn(SearchResultEntityFixture.aListOfSongSearchResultEntities());

        List<SearchResultEntity> searchResultEntities = searchService.findEntitiesByName("mess");

        assertNotNull(searchResultEntities);
        assertEquals(5, searchResultEntities.size());
        verify(mockedTypedQueryAlbum).setParameter("name", "mess");
        verify(mockedTypedQueryInterpret).setParameter("name", "mess");
        verify(mockedTypedQuerySong).setParameter("name", "mess");
    }

    @Test
    public void thatFindEntitiesByNameReturnEmptyListIfNoEntityWasFound() throws Exception {
        mockedEntityManager = mock(EntityManager.class);
        TypedQuery<SearchResultEntity> mockedTypedQueryAlbum = mock(TypedQuery.class);
        TypedQuery<SearchResultEntity> mockedTypedQueryInterpret = mock(TypedQuery.class);
        TypedQuery<SearchResultEntity> mockedTypedQuerySong = mock(TypedQuery.class);
        when(mockedCrudService.getEntityManager()).thenReturn(mockedEntityManager);
        when(mockedEntityManager.createQuery("SELECT NEW de.thi.mymusic.domain.SearchResultEntity(a.title, 'Album') FROM Album as a WHERE UPPER(a.title) like CONCAT('%', Upper(:name), '%')",
                SearchResultEntity.class)).thenReturn(mockedTypedQueryAlbum);
        when(mockedEntityManager.createQuery("SELECT NEW de.thi.mymusic.domain.SearchResultEntity(i.name, 'Interpret') FROM Interpret as i WHERE UPPER(i.name) like CONCAT('%', Upper(:name), '%')",
                SearchResultEntity.class)).thenReturn(mockedTypedQueryInterpret);
        when(mockedEntityManager.createQuery("SELECT NEW de.thi.mymusic.domain.SearchResultEntity(s.title, 'Song') FROM Song as s WHERE UPPER(s.title) like CONCAT('%', Upper(:name), '%')",
                SearchResultEntity.class)).thenReturn(mockedTypedQuerySong);
        when(mockedTypedQueryAlbum.getResultList()).thenReturn(null);
        when(mockedTypedQueryInterpret.getResultList()).thenReturn(null);
        when(mockedTypedQuerySong.getResultList()).thenReturn(null);

        List<SearchResultEntity> searchResultEntities = searchService.findEntitiesByName("mess");

        assertNotNull(searchResultEntities);
        assertEquals(0, searchResultEntities.size());
        verify(mockedTypedQueryAlbum).setParameter("name", "mess");
        verify(mockedTypedQueryInterpret).setParameter("name", "mess");
        verify(mockedTypedQuerySong).setParameter("name", "mess");
    }


}
