package de.thi.mymusic.domain;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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

    private String name;

    @OneToMany(mappedBy="interpret")
    private List<Album> albums;

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
