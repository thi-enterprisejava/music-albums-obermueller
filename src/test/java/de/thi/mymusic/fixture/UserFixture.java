package de.thi.mymusic.fixture;

import de.thi.mymusic.domain.User;

/**
 * Created by Michael on 13.12.2015.
 */
public class UserFixture {

    public static User aUser() {
        User testUser = new User();
        testUser.setUsername("Michael");
        testUser.setPassword("testPassword");

        return testUser;
    }

    public static User aStoredUser() {
        User storedUser = new User();
        storedUser.setUsername("storedMichael");
        storedUser.setPassword("7NcYcNGWMxapfjrDQIyYNa2M8PPBvHA1J8MCZVNPda4="); // test123

        return storedUser;
    }
}
