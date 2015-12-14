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
}
