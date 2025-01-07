package com.jsfcourse.property;

import java.io.Serializable;
import java.sql.Timestamp;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import com.jsf.dao.RoleDAO;
import com.jsf.dao.UserDAO;
import com.jsf.dao.UserroleDAO;
import com.jsf.entities.Role;
import com.jsf.entities.User;
import com.jsf.entities.Userrole;
import com.jsf.entities.UserrolePK;

@Named
@ViewScoped
public class RegisterBB implements Serializable {
    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
    private boolean termsAccepted;

    @EJB
    private UserDAO userDAO;

    @EJB
    private UserroleDAO userroleDAO;

    @EJB
    private RoleDAO roleDAO;

    @Inject
    private FacesContext context;

    public String register() {
        System.out.println("DEBUG: [RegisterBB.register] Enter method");

        try {
            //Walidacja emaila
            if (email == null || email.isBlank()) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Musisz podać adres email!", null));
                return null;
            }

            //Czy email już istnieje
            if (userDAO.isEmailUsed(email)) {
                System.out.println("DEBUG: [RegisterBB.register] Email already used: " + email);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ten adres email jest już zarejestrowany w systemie!", null));
                return null;
            }

            //Sprawdzam hasło
            System.out.println("DEBUG: [RegisterBB.register] password=" + password
                               + ", confirmPassword=" + confirmPassword);

            //Czy niepuste
            if (password == null || password.isBlank()) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Hasło nie może być puste!", null));
                return null;
            }
            if (confirmPassword == null || confirmPassword.isBlank()) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Musisz potwierdzić hasło!", null));
                return null;
            }
            //Czy identyczne
            if (!password.equals(confirmPassword)) {
                System.out.println("DEBUG: [RegisterBB.register] Passwords do not match");
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Hasła nie są identyczne!", null));
                return null;
            }
            //Czy ma 4 znaki zrób potem jeszcze duze litery i na jakieś 8 znaków zmien
            if (password.length() < 4) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Hasło musi mieć co najmniej 8 znaków!", null));
                return null;
            }

            //Czy zaznaczono regulamin
            if (!termsAccepted) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Musisz zaakceptować regulamin, aby się zarejestrować!", null));
                return null;
            }

            
            String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
            //Tworzy User
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(hashed); // w realu: szyfrowanie/hashing!
            user.setDateCreated(new Timestamp(System.currentTimeMillis()));

            System.out.println("DEBUG: [RegisterBB.register] user (before persist) = " + user);

            //Zapis User w bazie
            userDAO.create(user);
            System.out.println("DEBUG: [RegisterBB.register] userDAO.create done. user.getIdUser()="
                                + user.getIdUser());

            //Wymuś flush, by ID się uzupełniło
            userDAO.flush();
            System.out.println("DEBUG: [RegisterBB.register] flush done. user.getIdUser()="
                                + user.getIdUser());

            //Przypisanie usera do tego nowego
            Role roleEntity = roleDAO.find(1);
            if (roleEntity == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Błąd: rola ID=1 nie istnieje w bazie!", null));
                return null;
            }

            Userrole userrole = new Userrole();
            UserrolePK pk = new UserrolePK();
            pk.setIdUser(user.getIdUser());
            pk.setIdRole(roleEntity.getIdRole());
            userrole.setId(pk);

            userrole.setAssignedDate(new Timestamp(System.currentTimeMillis()));
            userrole.setRole(roleEntity);
            userrole.setUser(user);

            userroleDAO.create(userrole);

            //Sukces pog
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Rejestracja przebiegła pomyślnie. Możesz się zalogować.", null));

            //Przekierowanie na stronę logowania
            return "login?faces-redirect=true";

        } catch (Exception e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Wystąpił błąd podczas rejestracji użytkownika. " + e.getMessage(), null));
            return null;
        }
    }

    // Gettery/Settery
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    public boolean isTermsAccepted() { return termsAccepted; }
    public void setTermsAccepted(boolean termsAccepted) { this.termsAccepted = termsAccepted; }
}
