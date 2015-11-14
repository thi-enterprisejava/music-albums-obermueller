package de.thi.mymusic.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Functional data model of a interpret
 */

@Entity
public class Interpret {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private String name;


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


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
