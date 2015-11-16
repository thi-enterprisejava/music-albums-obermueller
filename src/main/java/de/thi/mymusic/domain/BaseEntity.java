package de.thi.mymusic.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Michael on 16.11.2015.
 *
 * Abstract BaseEntity with Entity Id and creationDate
 *
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // Only Date will be saved in timestamp
    @Temporal(TemporalType.DATE)
    private Date creationDate;


    //************************************************
    // Getter and Setter
    //************************************************


    public long getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
