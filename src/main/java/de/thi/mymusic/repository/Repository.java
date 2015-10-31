package de.thi.mymusic.repository;

import java.util.List;

/**
 * Created by Michael on 30.10.2015.
 */

public interface Repository<T> {

    List<T> fetchAll();

    List<T> fetchByName(String name);

    T findById(long id);

    void add(T object);

    void update(T object);

    void remove(T object);

}