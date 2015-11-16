package de.thi.mymusic.web.model;

import de.thi.mymusic.domain.Album;
import de.thi.mymusic.domain.Interpret;
import de.thi.mymusic.domain.Song;
import de.thi.mymusic.service.SearchService;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 05.11.2015.
 */

//TODO Implement Search Model
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

        return "listSearchResult";
    }

    // AJAX Request: Complete Search Input
    public List<String> completeSearchInput(String query) {
        List<String> results = new ArrayList<String>();
        List<Album> foundedResults = searchService.findAlbumByName(query);

        System.out.println(foundedResults.toString());

        // List of top 5 founded albums
        if(foundedResults != null) {
            int result_size = 5;
            if(foundedResults.size() < 5) {
                result_size = foundedResults.size();
            }
            for(int i = 0; i < result_size; i++) {
                if(foundedResults.get(i) != null) {
                    results.add(foundedResults.get(i).getTitle());
                }
            }
        }

        return results;
    }
}
