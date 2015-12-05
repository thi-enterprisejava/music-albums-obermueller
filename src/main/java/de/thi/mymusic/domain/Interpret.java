package de.thi.mymusic.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Functional data model of a interpret
 */

@Entity
@NamedQueries({
        @NamedQuery(name = "Interpret.findByName",
                query = "SELECT i FROM Interpret i WHERE UPPER(i.name) like CONCAT('%', UPPER(:name), '%')"),
        @NamedQuery(name = "Interpret.findByExactName",
                query = "SELECT i FROM Interpret i WHERE i.name like :name"),
})

public class Interpret extends BaseEntity {

    @NotNull
    private String name;

    @OneToMany(mappedBy="interpret")
    @JsonIgnore
    private List<Album> albums = new ArrayList<>();

    //************************************************
    // Constructors
    //************************************************

    public Interpret() {

    }

    public Interpret(String name) {
        this.name = name;
    }

    //************************************************
    // Getter and Setter
    //************************************************

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Album> getAlbums() {
        return Collections.unmodifiableList(albums);
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public void addAlbum(Album album) {
        this.albums.add(album);
    }

    public void removeAlbum(Album album) {
        this.albums.remove(album);
    }

    //************************************************
    // Equals and HashCode
    //************************************************

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interpret interpret = (Interpret) o;
        return Objects.equals(name, interpret.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
