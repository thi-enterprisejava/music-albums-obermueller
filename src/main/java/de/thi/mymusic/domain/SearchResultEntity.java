package de.thi.mymusic.domain;

import java.util.Objects;

/**
 * Entity model of a search result entity
 * Can be a name of a album, interpret or song
 */
public class SearchResultEntity {

    private String name;
    private String entityGroup;

    //************************************************
    // Constructors
    //************************************************

    public SearchResultEntity(String name, String entityGroup) {
        this.name = name;
        this.entityGroup = entityGroup;
    }

    //************************************************
    // Getter and Setter
    //************************************************

    public String getEntityGroup() {
        return entityGroup;
    }

    public void setEntityGroup(String entityGroup) {
        this.entityGroup = entityGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }

    //************************************************
    // Equals and HashCode
    //************************************************

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchResultEntity that = (SearchResultEntity) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(entityGroup, that.entityGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, entityGroup);
    }
}
