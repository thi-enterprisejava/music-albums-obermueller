package de.thi.mymusic.service;

import de.thi.mymusic.dao.CrudService;
import de.thi.mymusic.domain.User;
import org.apache.log4j.Logger;
import org.jboss.security.auth.spi.Util;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by Michael on 06.12.2015.
 */
@Stateless
public class UserService {

    @EJB
    private CrudService crudService;
    private static final Logger LOGGER = Logger.getLogger(UserService.class);


    public void setCrudService(CrudService crudService) {
        this.crudService = crudService;
    }

    @PermitAll
    public User findByUsername(String username) {
        List<User> users =  crudService.findByNamedQuery(User.class, "User.findByName",
                new String[] {"username"}, new Object[] {username});

        if(users != null && !users.isEmpty()) {
            User user = users.get(0);
            crudService.getEntityManager().detach(user);
            return user;
        } else {
            return null;
        }
    }

    @PermitAll
    public User findById(long id) {
        User user = crudService.findById(User.class, id);
        if(user != null) {
            crudService.getEntityManager().detach(user);
        }

        return crudService.findById(User.class, id);
    }

    @PermitAll
    public List<User> findAll(){

        List<User> users = crudService.findAll(User.class);
        users.forEach(user -> crudService.getEntityManager().detach(user));

        return crudService.findAll(User.class);
    }

    @RolesAllowed("Admin")
    public User createOrUpdate(User user) {
        if(user.getId() > 0) {
            update(user);
        } else {
            create(user);
        }
        return user;
    }

    @RolesAllowed("Admin")
    private void update(User user){
        User oldUser = findById(user.getId());
        LOGGER.info("Old password: " + oldUser.getPassword() + " new password: " + user.getPassword());
        if(!user.getPassword().equals(oldUser.getPassword())) {
            hashPassword(user);
        }
        crudService.merge(user);
        LOGGER.info("Update user: " + user.getUsername() +" with id: " + user.getId());
    }

    @RolesAllowed("Admin")
    private void create(User user){
        hashPassword(user);
        crudService.persist(user);
        LOGGER.info("Create new user: "+ user.getUsername() + " with id: " + user.getId());
    }

    @RolesAllowed("Admin")
    public void delete(User user) {
        LOGGER.info("Delete-User: " + user.getUsername());
        crudService.delete(user);
    }

    @RolesAllowed({"User", "Admin"})
    public void changePassword(User user) {
        User oldUser = findById(user.getId());
        if(!user.getPassword().equals(oldUser.getPassword())) {
            hashPassword(user);
        }
        crudService.merge(user);
        LOGGER.info("Change-Password from user: " + user.getUsername());
    }

    @RolesAllowed({"User", "Admin"})
    private void hashPassword(User user) {
        user.setPassword(Util.createPasswordHash("SHA-256", "BASE64", "UTF-8", null, user.getPassword()));
        LOGGER.info("Rehash Password for userId:" + user.getId() + " with username: " + user.getUsername());
    }

}
