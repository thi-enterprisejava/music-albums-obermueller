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
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.ejb.EJBAccessException;
import java.io.File;

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
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/web.xml"), "web.xml")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                ;
        System.out.println(webarchive.toString(Formatters.VERBOSE));

        return webarchive;
    }

    /**
     * method under test: createOrUpdate
     */

    @Test
    public void ThatUserCanBeAddedWithAdminRole() throws Exception {
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
    public void ThatUserCanNotAddedorUpdatedWithUserRole() throws Exception {
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
    public void ThatUserCanNotBeAddedOrUpdatedAnonymous() throws Exception {
        User userMichael = UserFixture.aUser();

        userService.createOrUpdate(userMichael);

    }

    @Test
    public void ThatUserCanBeUpdated() throws Exception {
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
    public void ThatDeleteIsPossibleAsRoleAdmin() throws Exception {
        authenticatedWithRoleAdmin.run(() -> {
            User userMichael = UserFixture.aUser();
            userService.createOrUpdate(userMichael);
            User foundedUser = userService.findByUsername(userMichael.getUsername());

            userService.delete(foundedUser);

            User foundedUserAfterDelete = userService.findByUsername(userMichael.getUsername());
            assertNull(foundedUserAfterDelete);
        });
    }

    @Test(expected = EJBAccessException.class)
    public void ThatDeleteIsNotPossibleAsRoleUser() throws Exception {
        authenticatedWithRoleUser.run(() ->

            userService.delete(UserFixture.aUser())
        );
    }

    @Test(expected = EJBAccessException.class)
    public void ThatDeleteIsNotPossibleAsAnonymous() throws Exception {

        userService.delete(UserFixture.aUser());

    }

    /**
     * method under test: findByUsername
     */

    @Test
    public void ThatFindByUsernameReturnsCorrectUser() throws Exception {
        authenticatedWithRoleAdmin.run(() ->
            userService.createOrUpdate(UserFixture.aUser())
        );

        User foundedUser = userService.findByUsername(UserFixture.aUser().getUsername());

        assertNotNull(foundedUser);
        assertEquals(UserFixture.aUser().getUsername(), foundedUser.getUsername());
        assertEquals(UserFixture.hashedPasswordFromAUser(), foundedUser.getPassword());

        authenticatedWithRoleAdmin.run(() ->
            userService.delete(foundedUser)
        );
    }

    @Test
    public void ThatFindByUsernameReturnsNullIfNoUserWasFounded() throws Exception {

        User foundedUser = userService.findByUsername(UserFixture.aUser().getUsername());

        assertNull(foundedUser);
    }

    /**
     * method under test: findById
     */

    @Test
    public void ThatFindByIdReturnsCorrectUser() throws Exception {
        authenticatedWithRoleAdmin.run(() ->
                userService.createOrUpdate(UserFixture.aUser())
        );
        User createdUser = userService.findByUsername(UserFixture.aUser().getUsername());

        User foundedUser = userService.findById(createdUser.getId());

        assertNotNull(foundedUser);
        assertEquals(UserFixture.aUser().getUsername(), foundedUser.getUsername());
        assertEquals(UserFixture.hashedPasswordFromAUser(), foundedUser.getPassword());
        authenticatedWithRoleAdmin.run(() ->
                userService.delete(foundedUser)
        );
    }

    @Test
    public void ThatFindByIdReturnsNullIfNoUserWasFound() throws Exception {

        User foundedUser = userService.findById(2000000000L);

        assertNull(foundedUser);
    }

    /**
     * method under test: changePassword
     */

    @Test
    public void ThatChangePasswordCanBeAccessedAsRoleAdmin() throws Exception {
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
    public void ThatChangePasswordCanBeAccessedAsRoleUser() throws Exception {
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
    public void ThatChangePasswordNotChangesPasswordIfPasswordNotBeUpdated() throws Exception {
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
    public void ThatChangePasswordCanNotBeAccessedAsAnonymous() throws Exception {

        userService.changePassword(UserFixture.aUser());

    }
}
