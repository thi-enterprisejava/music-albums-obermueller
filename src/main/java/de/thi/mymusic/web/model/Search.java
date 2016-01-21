package de.thi.mymusic.web.model;

import de.thi.mymusic.domain.Album;
import de.thi.mymusic.domain.Interpret;
import de.thi.mymusic.domain.SearchResultEntity;
import de.thi.mymusic.domain.Song;
import de.thi.mymusic.service.SearchService;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Michael on 05.11.2015.
 */
@Named
@SessionScoped
public class Search implements Serializable {

    private List<Album> albumResult;
    private List<Song> songResult;
    private List<Interpret> interpretResult;
    private String searchString;
    private SearchService searchService;

    @Inject
    public Search(SearchService searchService) {
        this.searchService = searchService;
    }

    //*******************************************************
    // Getter and Setter
    //*******************************************************


    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public List<Album> getAlbumResult() {
        return albumResult;
    }

    public List<Song> getSongResult() {
        return songResult;
    }

    public List<Interpret> getInterpretResult() {
        return interpretResult;
    }

    public long getCountAlbumResult() {
        return albumResult.size();
    }

    public long getCountInterpretResult() {
        return interpretResult.size();
    }

    public long getCountSongResult() {
        return songResult.size();
    }


    //*******************************************************
    // Action Methods
    //*******************************************************
    public String doSearch(){
        albumResult = searchService.findAlbumByName(searchString);
        songResult = searchService.findSongByName(searchString);
        interpretResult = searchService.findInterpretByName(searchString);

        // Reset search string
        searchString = "";
        return "listSearchResult";
    }


    /**
     * CompleteSearchInput is responsible for ajax auto complete search values
     * @param query is search string
     * @return List of five SearchResultEntity
     */
    public List<SearchResultEntity> completeSearchInput(String query) {
        return searchService.findEntitiesByName(query);
    }
}
