package de.thi.mymusic.fixture;

import de.thi.mymusic.domain.User;
import de.thi.mymusic.domain.UserRole;

import java.util.Arrays;

/**
 * Created by Michael on 13.12.2015.
 */
public class UserFixture {

    public static User aUser() {
        User testUser = new User();
        testUser.setUsername("Michael");
        testUser.setPassword("testPassword"); // Hash-Wert: /Vy1G6/WD2/b7d5uYsRz2m8kfbJxYz4VkZureKAu6es=

        return testUser;
    }

    public static String hashedPasswordFromAUser() {
        return "/Vy1G6/WD2/b7d5uYsRz2m8kfbJxYz4VkZureKAu6es=";
    }

    public static User aStoredUserWithId() {
        User storedUser = new User();
        storedUser.setUsername("storedMichael");
        storedUser.setPassword("7NcYcNGWMxapfjrDQIyYNa2M8PPBvHA1J8MCZVNPda4="); // test123
        storedUser.setId(2L);

        return storedUser;
    }

    public static User aUserWithId() {
        User testUser = new User();
        testUser.setUsername("Michael");
        testUser.setPassword("testPassword"); // Hash-Wert: /Vy1G6/WD2/b7d5uYsRz2m8kfbJxYz4VkZureKAu6es=
        testUser.setId(2L);

        return testUser;
    }

    public static User aUserWithRoleUser() {
        User testUser = new User();
        testUser.setUsername("MichaelUser");
        testUser.setPassword("testPassword"); // Hash-Wert: /Vy1G6/WD2/b7d5uYsRz2m8kfbJxYz4VkZureKAu6es=
        testUser.setId(3L);
        testUser.setUserRoles(Arrays.asList(new UserRole("User")));

        return testUser;
    }

    public static User aUserWithRoleAdmin() {
        User testUser = new User();
        testUser.setUsername("MichaelAdmin");
        testUser.setPassword("testPassword"); // Hash-Wert: /Vy1G6/WD2/b7d5uYsRz2m8kfbJxYz4VkZureKAu6es=
        testUser.setId(4L);
        testUser.setUserRoles(Arrays.asList(new UserRole("User"), new UserRole("Admin")));

        return testUser;
    }
}
