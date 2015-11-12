package de.thi.mymusic.web.model;

import de.thi.mymusic.domain.Album;
import de.thi.mymusic.repository.Repository;

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

    private List<Album> result;
    private String searchString;
    private Repository albumRepository;

    @Inject
    public Search(Repository<Album> albumRepository) {
        this.albumRepository = albumRepository;
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

    public List<Album> getResult() {
        return result;
    }


    //*******************************************************
    // Action Methods
    //*******************************************************
    public String doSearch(){
        result = albumRepository.findByName(searchString);

        // Only one match show album detail page
        if(result.size() == 1) {
            return "detailAlbum.xhtml?faces-redirect=true&album="+result.get(0).getTitle();
        } else {

            return "listSearchResult";
        }
    }

    // AJAX Request: Complete Search Input
    public List<String> completeSearchInput(String query) {
        List<String> results = new ArrayList<String>();
        List<Album> foundedResults = albumRepository.findByName(query);

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
