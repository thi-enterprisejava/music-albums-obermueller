package de.thi.mymusic.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
  *
 * Abstract BaseEntity with Entity Id, creationTimestamp and updateTimestamp
 *
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // Only Date will be saved in timestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @JsonIgnore
    private Date creationTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @JsonIgnore
    private Date updateTimestamp;

    //************************************************
    // Getter and Setter
    //************************************************

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    @PrePersist
    protected void prePersist() {
        updateTimestamp = creationTimestamp = new Date();
    }

    @PreUpdate
    protected void preUpdate() {
        updateTimestamp = new Date();
    }
}
