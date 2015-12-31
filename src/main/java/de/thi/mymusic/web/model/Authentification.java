package de.thi.mymusic.web.model;

import de.thi.mymusic.service.UserService;
import de.thi.mymusic.util.GuiUtils;


import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Authentification is responsible for login and logout of a user
 */

@Named
@RequestScoped
public class Authentification implements Serializable {


    private String username;
    private String password;
    private UserService userService;
    private GuiUtils guiUtils;


    @Inject
    public Authentification(UserService userService, GuiUtils guiUtils) {
        this.userService = userService;
        this.guiUtils = guiUtils;
    }

    //*******************************************************
    // Getter and Setter
    //*******************************************************

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //*******************************************************
    // Action Methods
    //*******************************************************

    public String doLogin(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)
                context.getExternalContext().getRequest();

        try {
            request.login(this.username, this.password);
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage("Login failed."));
            return "/loginerror.xhtml";
        }

        if("/login.xhtml".equals(context.getViewRoot().getViewId()) || "/loginerror.xhtml".equals(context.getViewRoot().getViewId())) {
            return "/search.xhtml";
        }

        return null;
    }

    public void doLogout() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.invalidateSession();
        externalContext.setResponseStatus(401);
        externalContext.getResponseOutputWriter().write("<html><head><meta http-equiv='refresh' content='0;search.xhtml'></head></html>");
        facesContext.responseComplete();
    }

    public boolean checkNavigationLoginDisable() {
        FacesContext context = FacesContext.getCurrentInstance();

        if("/login.xhtml".equals(context.getViewRoot().getViewId())) {
            return true;
        } else {
            return false;
        }
    }
}
