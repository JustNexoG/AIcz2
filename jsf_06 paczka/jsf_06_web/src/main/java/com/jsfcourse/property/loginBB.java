package com.jsfcourse.property;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;
import com.jsf.dao.UserDAO;
import com.jsf.entities.User;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;

@Named
@SessionScoped
public class loginBB implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private boolean rememberMe;
    private User loggedUser;

    @Inject
    private FacesContext context;

    @Inject
    private UserDAO userDAO;

    public String login() {
        try {
            User user = userDAO.findByEmail(username);
            if(user == null || !BCrypt.checkpw(password, user.getPassword())) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Błędne dane logowania", null));
                return null;
            }
            loggedUser = user;
            return "main.xhtml?faces-redirect=true";
        } catch(Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Wystąpił błąd podczas logowania.", null));
            e.printStackTrace();
            return null;
        }
    }

    public String goToAddProperty() {
        if(loggedUser == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Musisz być zalogowany", null));
            return "login.xhtml?faces-redirect=true";
        }
        return "propertyEdit.xhtml?faces-redirect=true";
    }

    public String logout() {
        loggedUser = null;
        context.getExternalContext().invalidateSession();
        return "main.xhtml?faces-redirect=true";
    }

    // Gettery i settery
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean isRememberMe() { return rememberMe; }
    public void setRememberMe(boolean rememberMe) { this.rememberMe = rememberMe; }
    public User getLoggedUser() { return loggedUser; }
}