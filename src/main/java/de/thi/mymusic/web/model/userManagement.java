package de.thi.mymusic.web.model;

import de.thi.mymusic.domain.User;
import de.thi.mymusic.service.UserService;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Michael on 05.12.2015.
 */

@Named
@ViewScoped
public class UserManagement implements Serializable {

    private UserService userService;
    private User user;
    private long userId;

    @Inject
    public UserManagement(UserService userService) {
        this.userService = userService;
        user = new User();
    }

    public String init(){
        if(userId > 0) {
            user = userService.findById(userId);

            if(user == null) {
                //TODO Translate Message String
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "User konnte nicht gefunden werden!", null);
                FacesContext.getCurrentInstance().addMessage(null, message);

                return "error";
            }
        }

        return null;
    }

    public String initForPasswordChange(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)
                context.getExternalContext().getRequest();

        if(request.getUserPrincipal() != null) {
            user = userService.findByUsername(request.getUserPrincipal().getName());

            if(user != null) {
                userId = user.getId();
                return null;
            }
        }

        return "/error.xhtml";
    }

    //*******************************************************
    // Getter and Setter
    //*******************************************************

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<User> allUsers() {
        return userService.findAll();
    }

    //*******************************************************
    // Action Methods
    //*******************************************************

    public String doSave() {
        userService.createOrUpdate(this.user);

        return "editUser.xhtml";
    }

    public String doCancel() {
        if(userId > 0) {
            init();
            return "editUser.xhtml?user=" + userId;
        } else {
            user = new User();
        }

        return "editUser.xhtml";
    }

    public String doChangePassword() {
        userService.createOrUpdate(this.user);

        return null;
    }

    public String doCancelChangePassword() {
        initForPasswordChange();

        return "changePassword.xhtml";
    }
}
