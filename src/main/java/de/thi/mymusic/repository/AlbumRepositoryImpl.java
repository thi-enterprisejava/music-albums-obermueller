package de.thi.mymusic.repository;

import de.thi.mymusic.domain.Album;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Michael on 30.10.2015.
 */

@ApplicationScoped
public class AlbumRepositoryImpl implements Repository<Album> {

    private static final List<Album> albumList = new ArrayList<>();

    @Override
    public List<Album> findAll() {
        return albumList;
    }

    @Override
    public List<Album> findByName(String name) {
        return albumList
                .stream()
                .filter(album -> album.getTitle().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    // TODO Implement method findById
    public Album findById(long id) {
        return null;
    }

    @Override
    public void add(Album object) {
        albumList.add(object);
    }

    @Override
    // TODO Implement method update
    public void update(Album object) {

    }

    @Override
    public void remove(Album object) {
        if(albumList.contains(object)) {
            albumList.remove(object);
        }
    }
}
