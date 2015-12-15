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
import javax.transaction.Transactional;

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
                /*.addAsResource("sql/data.sql", "META-INF/sql/data.sql")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/faces-config.xml"), "faces-config.xml")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/mymusic.taglib.xml"), "mymusic.taglib.xml")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/faces-config.xml"), "faces-config.xml")
                .addAsResource(new File("src/main/resources/messages_de.properties"), "messages_de.properties")
                .addAsResource(new File("src/main/resources/messages_en.properties"), "messages_en.properties")*/

                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                ;
        System.out.println(webarchive.toString(Formatters.VERBOSE));

        return webarchive;
    }

    @Test
    @Ignore
    public void That() throws Exception {
        authenticatedWithRoleAdmin.call(() -> {
            User user = new User();
            user.setUsername("NeuerTestUser");
            user.setPassword("huhu123");

            userService.createOrUpdate(user);

            User user2 = userService.findByUsername("NeuerTestUser");
            assertNotNull(user2);
            assertEquals(user.getUsername(), user2.getUsername());
            assertEquals(user.getPassword(), user2.getPassword());

            return null;
        });
    }

    /**
     * method under test: createOrUpdate
     *
     */

    @Test
    public void ThatUserCanBeAddedWithAdminRole() throws Exception {
        authenticatedWithRoleAdmin.call(() -> {
            User userMichael = UserFixture.aUser();

            userService.createOrUpdate(userMichael);

            User savedUser = userService.findByUsername("Michael");
            assertEquals("/Vy1G6/WD2/b7d5uYsRz2m8kfbJxYz4VkZureKAu6es=", savedUser.getPassword());
            assertNotNull(savedUser);
            assertEquals(userMichael, savedUser);
            assertNotEquals(0, savedUser.getId());
            userService.delete(savedUser);
            return null;
        });
    }

    @Test
    public void ThatUserCanNotAddedWithUserRole() throws Exception {
        authenticatedWithRoleUser.call(() -> {
            User userMichael = UserFixture.aUser();

            try {
                userService.createOrUpdate(userMichael);

                fail("Should throw an EJBAccessException ");
            } catch(EJBAccessException ex) {
            }
            return null;
        });
    }

    @Test(expected = EJBAccessException.class)
    public void ThatUserCanNotBeAddedAnonymous() throws Exception {
        User userMichael = UserFixture.aUser();

        userService.createOrUpdate(userMichael);

    }

    @Test
    public void ThatUserCanBeUpdated() throws Exception {
        authenticatedWithRoleAdmin.call(() -> {
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

            return null;
        });
    }


}
