package de.thi.mymusic.domain;

/**
 * Functional data model of a interpret
 */
public class Interpret {

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

        return !(name != null ? !name.equals(interpret.name) : interpret.name != null);

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
