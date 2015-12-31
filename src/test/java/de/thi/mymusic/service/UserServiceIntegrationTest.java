package de.thi.mymusic.service;

import de.thi.mymusic.dao.CrudService;
import de.thi.mymusic.domain.BaseEntity;
import de.thi.mymusic.domain.User;
import de.thi.mymusic.domain.UserRole;
import de.thi.mymusic.fixture.UserFixture;
import de.thi.mymusic.security.AuthenticatedWithRoleAdmin;
import de.thi.mymusic.security.AuthenticatedWithRoleUser;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.ejb.EJBAccessException;

import static org.junit.Assert.*;

/**
 * Created by Michael on 14.12.2015.
 */

@RunWith(Arquillian.class)
public class UserServiceIntegrationTest {
    /**
     * Class under test
     */
    @EJB
    UserService userService;

    @EJB
    AuthenticatedWithRoleAdmin authenticatedWithRoleAdmin;
    @EJB
    AuthenticatedWithRoleUser authenticatedWithRoleUser;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive webarchive = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClass(BaseEntity.class)
                .addClass(UserService.class)
                .addClass(User.class)
                .addClass(UserRole.class)
                .addClass(CrudService.class)
                .addClass(UserFixture.class)
                .addClass(AuthenticatedWithRoleAdmin.class)
                .addClass(AuthenticatedWithRoleUser.class)
                //.addAsWebInfResource(new File("src/main/webapp/WEB-INF/web.xml"), "web.xml")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("mymusictest-ds.xml")
                ;
        System.out.println(webarchive.toString(Formatters.VERBOSE));

        return webarchive;
    }

    /**
     * method under test: createOrUpdate
     */

    @Test
    public void thatUserCanBeAddedWithAdminRole() throws Exception {
        authenticatedWithRoleAdmin.run(() -> {
            User userMichael = UserFixture.aUser();

            userService.createOrUpdate(userMichael);

            User savedUser = userService.findByUsername("Michael");
            assertEquals("/Vy1G6/WD2/b7d5uYsRz2m8kfbJxYz4VkZureKAu6es=", savedUser.getPassword());
            assertNotNull(savedUser);
            assertEquals(userMichael.getUsername(), savedUser.getUsername());
            assertNotEquals(0, savedUser.getId());
            userService.delete(savedUser);
        });
    }

    @Test
    public void thatUserCanNotAddedOrUpdatedWithUserRole() throws Exception {
        authenticatedWithRoleUser.run(() -> {
            User userMichael = UserFixture.aUser();

            try {
                userService.createOrUpdate(userMichael);

                fail("Should throw an EJBAccessException ");
            } catch(EJBAccessException ex) {
            }
        });
    }

    @Test(expected = EJBAccessException.class)
    public void thatUserCanNotBeAddedOrUpdatedAnonymous() throws Exception {
        User userMichael = UserFixture.aUser();

        userService.createOrUpdate(userMichael);

    }

    @Test
    public void thatUserCanBeUpdated() throws Exception {
        authenticatedWithRoleAdmin.run(() -> {
            User userMichael = UserFixture.aUser();
            userService.createOrUpdate(userMichael);
            User savedUser = userService.findByUsername("Michael");
            long userId = savedUser.getId();
            savedUser.setUsername("Michael2");
            savedUser.setPassword("test123");

            userService.createOrUpdate(savedUser);

            assertNotEquals(0, savedUser.getId());
            User preUpdatedUser = userService.findByUsername("Michael");
            assertNull(preUpdatedUser);
            User updatedUser = userService.findByUsername("Michael2");
            assertNotNull(updatedUser);
            assertEquals(userId, updatedUser.getId());
            assertEquals("Michael2", updatedUser.getUsername());
            assertEquals("7NcYcNGWMxapfjrDQIyYNa2M8PPBvHA1J8MCZVNPda4=", updatedUser.getPassword());
            userService.delete(updatedUser);
        });
    }

    /**
     * method under test: delete
     */

    @Test
    public void thatDeleteIsPossibleAsRoleAdmin() throws Exception {
        authenticatedWithRoleAdmin.run(() -> {
            User userMichael = UserFixture.aUser();
            userService.createOrUpdate(userMichael);
            User foundUser = userService.findByUsername(userMichael.getUsername());

            userService.delete(foundUser);

            User foundUserAfterDelete = userService.findByUsername(userMichael.getUsername());
            assertNull(foundUserAfterDelete);
        });
    }

    @Test(expected = EJBAccessException.class)
    public void thatDeleteIsNotPossibleAsRoleUser() throws Exception {
        authenticatedWithRoleUser.run(() ->

            userService.delete(UserFixture.aUser())
        );
    }

    @Test(expected = EJBAccessException.class)
    public void thatDeleteIsNotPossibleAsAnonymous() throws Exception {

        userService.delete(UserFixture.aUser());

    }

    /**
     * method under test: findByUsername
     */

    @Test
    public void thatFindByUsernameReturnsCorrectUser() throws Exception {
        authenticatedWithRoleAdmin.run(() ->
            userService.createOrUpdate(UserFixture.aUser())
        );

        User foundUser = userService.findByUsername(UserFixture.aUser().getUsername());

        assertNotNull(foundUser);
        assertEquals(UserFixture.aUser().getUsername(), foundUser.getUsername());
        assertEquals(UserFixture.hashedPasswordFromAUser(), foundUser.getPassword());

        authenticatedWithRoleAdmin.run(() ->
            userService.delete(foundUser)
        );
    }

    @Test
    public void thatFindByUsernameReturnsNullIfNoUserWasFound() throws Exception {

        User foundUser = userService.findByUsername(UserFixture.aUser().getUsername());

        assertNull(foundUser);
    }

    /**
     * method under test: findById
     */

    @Test
    public void thatFindByIdReturnsCorrectUser() throws Exception {
        authenticatedWithRoleAdmin.run(() ->
                userService.createOrUpdate(UserFixture.aUser())
        );
        User createdUser = userService.findByUsername(UserFixture.aUser().getUsername());

        User foundUser = userService.findById(createdUser.getId());

        assertNotNull(foundUser);
        assertEquals(UserFixture.aUser().getUsername(), foundUser.getUsername());
        assertEquals(UserFixture.hashedPasswordFromAUser(), foundUser.getPassword());
        authenticatedWithRoleAdmin.run(() ->
                userService.delete(foundUser)
        );
    }

    @Test
    public void thatFindByIdReturnsNullIfNoUserWasFound() throws Exception {

        User foundUser = userService.findById(2000000000L);

        assertNull(foundUser);
    }

    /**
     * method under test: changePassword
     */

    @Test
    public void thatChangePasswordCanBeAccessedAsRoleAdmin() throws Exception {
        authenticatedWithRoleAdmin.run(() -> {
            userService.createOrUpdate(UserFixture.aUser());
            User createdUser = userService.findByUsername(UserFixture.aUser().getUsername());
            createdUser.setPassword("test123");

            userService.changePassword(createdUser);

            User changedUser = userService.findByUsername(UserFixture.aUser().getUsername());
            assertNotNull(changedUser);
            assertEquals("7NcYcNGWMxapfjrDQIyYNa2M8PPBvHA1J8MCZVNPda4=", changedUser.getPassword());
            userService.delete(changedUser);
        });
    }

    @Test
    public void thatChangePasswordCanBeAccessedAsRoleUser() throws Exception {
        authenticatedWithRoleAdmin.run(() ->
                userService.createOrUpdate(UserFixture.aUser())
        );
        authenticatedWithRoleUser.run(() -> {
        User createdUser = userService.findByUsername(UserFixture.aUser().getUsername());
        createdUser.setPassword("test123");

            userService.changePassword(createdUser);

            User changedUser = userService.findByUsername(UserFixture.aUser().getUsername());
            assertNotNull(changedUser);
            assertEquals("7NcYcNGWMxapfjrDQIyYNa2M8PPBvHA1J8MCZVNPda4=", changedUser.getPassword());
        });
        authenticatedWithRoleAdmin.run(() -> {
            User deleteUser = userService.findByUsername(UserFixture.aUser().getUsername());
            userService.delete(deleteUser);
        });
    }

    @Test
    public void thatChangePasswordNotChangesPasswordIfPasswordNotBeUpdated() throws Exception {
        authenticatedWithRoleAdmin.run(() -> {
            userService.createOrUpdate(UserFixture.aUser());
            User createdUser = userService.findByUsername(UserFixture.aUser().getUsername());

            userService.changePassword(createdUser);

            User changedUser = userService.findByUsername(UserFixture.aUser().getUsername());
            assertNotNull(changedUser);
            assertEquals(UserFixture.hashedPasswordFromAUser(), changedUser.getPassword());
            userService.delete(changedUser);
        });
    }

    @Test(expected = EJBAccessException.class)
    public void thatChangePasswordCanNotBeAccessedAsAnonymous() throws Exception {

        userService.changePassword(UserFixture.aUser());

    }
}
