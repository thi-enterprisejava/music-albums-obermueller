package de.thi.mymusic.service;

import de.thi.mymusic.dao.CrudService;
import de.thi.mymusic.domain.User;
import org.apache.log4j.Logger;
import org.jboss.security.auth.spi.Util;

import javax.annotation.security.PermitAll;
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
    private static final Logger logger = Logger.getLogger(UserService.class);

    @PermitAll
    public User findByUsername(String username) {
        List<User> users =  crudService.findByNamedQuery(User.class, "User.findByName",
                new String[] {"username"}, new Object[] {username});

        if(users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

    @PermitAll
    public User findById(long id) {
        return crudService.findById(User.class, id);
    }

    @PermitAll
    public List<User> findAll(){
        return crudService.findAll(User.class);
    }


    @PermitAll
    public User createOrUpdate(User user) {
        if(user.getId() > 0) {
            update(user);
        } else {
            create(user);
        }
        return user;
    }

    @PermitAll
    private void update(User user){
        User oldUser = findById(user.getId());
        if(!user.getPassword().equals(oldUser.getPassword())) {
            hashPassword(user);
        }
        crudService.merge(user);
        logger.info("Update user: " + user.getUsername() +" with id: " + user.getId());
    }

    @PermitAll
    private void create(User user){
        hashPassword(user);
        crudService.persist(user);
        logger.info("Create new user: "+ user.getUsername() + " with id: " + user.getId());
    }

    @PermitAll
    private void hashPassword(User user) {
        user.setPassword(Util.createPasswordHash("SHA-256", "BASE64", "UTF-8", null, user.getPassword()));
    }
}
