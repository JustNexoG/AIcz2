package com.jsfcourse.property;

import java.io.Serializable;
import java.sql.Timestamp;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import com.jsf.dao.UserDAO;
import com.jsf.entities.User;
import com.jsfcourse.property.loginBB;

@Named
@SessionScoped
public class ProfileBB implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject 
    private FacesContext context;

    @Inject 
    private loginBB loginBB;

    @Inject 
    private UserDAO userDAO;
    
    private String email;
    private String firstName;
    private String lastName;
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
    
    public void loadProfile() {
        User currentUser = loginBB.getLoggedUser();
        if (currentUser != null) {
            email = currentUser.getEmail();
            firstName = currentUser.getFirstName();
            lastName = currentUser.getLastName();
        }
    }
    
    public String saveProfile() {
        User currentUser = loginBB.getLoggedUser();
        if (currentUser == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nie jesteś zalogowany.", null));
            return null;
        }
        
        // Walidacja aktualnego hasła
        if (!BCrypt.checkpw(currentPassword, currentUser.getPassword())) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nieprawidłowe aktualne hasło.", null));
            return null;
        }
        
        // Sprawdzenie i walidacja nowego hasła, jeśli zostało podane
        if (newPassword != null && !newPassword.isEmpty()) {
            if (!newPassword.equals(confirmNewPassword)) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nowe hasła nie są identyczne.", null));
                return null;
            }
            if (newPassword.length() < 8) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nowe hasło musi mieć co najmniej 8 znaków.", null));
                return null;
            }
            String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
            currentUser.setPassword(hashed);
        }
        
        // Aktualizacja danych użytkownika
        currentUser.setEmail(email);
        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        
        // Ustawienie daty i użytkownika modyfikującego
        currentUser.setLastModifiedBy(currentUser.getIdUser());
        currentUser.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
        
        try {
            userDAO.merge(currentUser);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Profil zaktualizowany pomyślnie.", null));
        } catch(Exception e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd podczas aktualizacji profilu.", null));
            return null;
        }
        return "main.xhtml?faces-redirect=true";
    }
    
    // Gettery i settery
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    public String getConfirmNewPassword() { return confirmNewPassword; }
    public void setConfirmNewPassword(String confirmNewPassword) { this.confirmNewPassword = confirmNewPassword; }
}
