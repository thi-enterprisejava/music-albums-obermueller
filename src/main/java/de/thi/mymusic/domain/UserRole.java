package de.thi.mymusic.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Created by Michael on 08.12.2015.
 */

@Entity
@Table(name = "UserRoles")
public class UserRole extends BaseEntity{

    @ManyToOne
    private User user;

    @NotNull
    private String role;

    //************************************************
    // Constructors
    //************************************************

    public UserRole(){
    }

    public UserRole(String role) {
        this.role = role;
    }

    //************************************************
    // Getter and Setter
    //************************************************

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //************************************************
    // Equals and HashCode
    //************************************************

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return Objects.equals(user, userRole.user) &&
                Objects.equals(role, userRole.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, role);
    }
}
