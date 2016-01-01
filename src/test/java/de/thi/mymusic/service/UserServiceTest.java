package de.thi.mymusic.service;

import org.jboss.security.auth.spi.Util;
import de.thi.mymusic.dao.CrudService;
import de.thi.mymusic.domain.User;
import de.thi.mymusic.fixture.UserFixture;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Michael on 23.12.2015.
 */
public class UserServiceTest {

    /**
     * class under test
     */
    UserService userService;

    CrudService mockedCrudService;
    EntityManager mockedEntityManager;

    @Before
    public void setUp() throws Exception {
        mockedCrudService = mock(CrudService.class);
        userService = new UserService();
        userService.setCrudService(mockedCrudService);
    }

    /**
     * method under test: findByUsername
     */

    @Test
    public void thatFindByUsernameReturnsCorrectUser() throws Exception {
       when(mockedCrudService.findByNamedQuery(User.class, "User.findByName",
                new String[] {"username"}, new Object[] {UserFixture.aUser()
                       .getUsername()})).thenReturn(Arrays.asList(UserFixture.aUser()));
        mockedEntityManager = mock(EntityManager.class);
        when(mockedCrudService.getEntityManager()).thenReturn(mockedEntityManager);

        User foundedUser = userService.findByUsername(UserFixture.aUser().getUsername());

        verify(mockedEntityManager).detach(UserFixture.aUser());
        assertEquals(UserFixture.aUser(), foundedUser);
    }

    @Test
    public void thatFindByUsernameReturnsNullIfNoUserWasFound() throws Exception {
        when(mockedCrudService.findByNamedQuery(User.class, "User.findByName",
                new String[] {"username"}, new Object[] {UserFixture.aUser()
                        .getUsername()})).thenReturn(null);

        User foundedUser = userService.findByUsername(UserFixture.aUser().getUsername());

        assertNull(foundedUser);
    }

    /**
     * method under test: findById
     */

    @Test
    public void ThatFindByIdReturnCorrectUser() throws Exception {
        when(mockedCrudService.findById(User.class, 2L)).thenReturn(UserFixture.aUserWithId());
        mockedEntityManager = mock(EntityManager.class);
        when(mockedCrudService.getEntityManager()).thenReturn(mockedEntityManager);

        User foundedUser = userService.findById(2L);

        assertEquals(UserFixture.aUserWithId(), foundedUser);
        verify(mockedEntityManager).detach(UserFixture.aUserWithId());
    }

    @Test
    public void thatFindByIdReturnNullIfNoUserWasFound() throws Exception {
        when(mockedCrudService.findById(User.class, 3L)).thenReturn(null);

        User foundedUser = userService.findById(2L);

        assertNull(foundedUser);

    }

    /**
     * method under test: findAll
     */

    @Test
    public void thatFindAllReturnListOfUsers() throws Exception {
        List<User> users = Arrays.asList(UserFixture.aUserWithRoleUser(),
                UserFixture.aUserWithRoleAdmin());
        when(mockedCrudService.findAll(User.class)).thenReturn(users);
        mockedEntityManager = mock(EntityManager.class);
        when(mockedCrudService.getEntityManager()).thenReturn(mockedEntityManager);

        List<User> foundedUsers = userService.findAll();

        assertEquals(foundedUsers.size(), 2);
        verify(mockedEntityManager, times(2)).detach(any(User.class));
    }

    @Test
    public void thatFindAllReturnEmptyListIfNoUsersWereFound() throws Exception {
        List<User> users = new ArrayList<>();
        when(mockedCrudService.findAll(User.class)).thenReturn(users);
        mockedEntityManager = mock(EntityManager.class);
        when(mockedCrudService.findAll(User.class)).thenReturn(users);

        List<User> foundedUsers = userService.findAll();

        assertEquals(foundedUsers.size(), 0);
        verify(mockedEntityManager, times(0)).detach(any(User.class));
    }

    /**
     * method under test: createOrUpdate
     */

    @Test
    public void thatCreateOrUpdateAddNewUserCorrect() throws Exception {
        User expectedUser = UserFixture.aUser();
        expectedUser.setPassword("/Vy1G6/WD2/b7d5uYsRz2m8kfbJxYz4VkZureKAu6es=");

        userService.createOrUpdate(UserFixture.aUser());

        verify(mockedCrudService).persist(expectedUser);
    }

    @Test
    public void thatCreateOrUpdateEditUserCorrect() throws Exception {
        User expectedUser = UserFixture.aUserWithId();
        expectedUser.setPassword("/Vy1G6/WD2/b7d5uYsRz2m8kfbJxYz4VkZureKAu6es=");
        when(mockedCrudService.findById(User.class, 2L)).thenReturn(UserFixture.aStoredUserWithId());
        mockedEntityManager = mock(EntityManager.class);
        when(mockedCrudService.getEntityManager()).thenReturn(mockedEntityManager);

        userService.createOrUpdate(UserFixture.aUserWithId());

        verify(mockedCrudService).merge(expectedUser);
    }

    @Test
    public void thatCreateOrUpdateNotRehashPasswordIfNotChanged() throws Exception {
        User expectedUser = UserFixture.aStoredUserWithId();
        expectedUser.setUsername("MichaelUpdated");
        when(mockedCrudService.findById(User.class, 2L)).thenReturn(UserFixture.aStoredUserWithId());
        mockedEntityManager = mock(EntityManager.class);
        when(mockedCrudService.getEntityManager()).thenReturn(mockedEntityManager);
        User updatedUser = UserFixture.aStoredUserWithId();
        updatedUser.setUsername("MichaelUpdated");

        userService.createOrUpdate(updatedUser);

        verify(mockedCrudService).merge(expectedUser);
    }

    /**
     * method under test: changePassword
     */

    @Test
    public void thatChangePasswordUpdateHashValueIfChanged() throws Exception {
        User expectedUser = UserFixture.aStoredUserWithId();
        User updatedUser = UserFixture.aStoredUserWithId();
        updatedUser.setPassword("test123");
        when(mockedCrudService.findById(User.class, 2L)).thenReturn(UserFixture.aStoredUserWithId());
        mockedEntityManager = mock(EntityManager.class);
        when(mockedCrudService.getEntityManager()).thenReturn(mockedEntityManager);

        userService.changePassword(updatedUser);

        verify(mockedCrudService).merge(expectedUser);
    }

    @Test
    public void thatChangePasswordNotUpdateHashIfPasswordNotChanged() throws Exception {
        when(mockedCrudService.findById(User.class, 2L)).thenReturn(UserFixture.aStoredUserWithId());
        mockedEntityManager = mock(EntityManager.class);
        when(mockedCrudService.getEntityManager()).thenReturn(mockedEntityManager);

        userService.changePassword(UserFixture.aStoredUserWithId());

        verify(mockedCrudService).merge(UserFixture.aStoredUserWithId());
    }

    /**
     * method under test: delete
     */

    @Test
    public void thatDeleteDoDeleteCorrectAlbum() throws Exception {

        userService.delete(UserFixture.aStoredUserWithId());

        verify(mockedCrudService).delete(UserFixture.aStoredUserWithId());
    }


}