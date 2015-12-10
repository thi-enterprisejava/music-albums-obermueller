package de.thi.mymusic.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Michael on 06.12.2015.
 */

@Entity
@NamedQueries({
        @NamedQuery(name = "User.findByName",
                query = "SELECT u FROM User u WHERE u.username like :username")
})

@Table(name = "Users")
public class User extends BaseEntity {

    @NotNull
    @Column(unique=true)
    private String username;

    @NotNull
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserRole> userRoles = new ArrayList<>();


    //************************************************
    // Constructors
    //************************************************

    public User() {
        // Add every user the needed basic role
        if(userRoles.size() == 0) {
            UserRole role = new UserRole("User");
            role.setUser(this);
            userRoles.add(role);
        }
    }

    //************************************************
    // Getter and Setter
    //************************************************

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //************************************************
    // Equals and HashCode
    //************************************************

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
