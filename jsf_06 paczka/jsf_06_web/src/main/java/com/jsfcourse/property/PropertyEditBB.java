package com.jsfcourse.property;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.primefaces.model.file.UploadedFile;
import com.jsf.entities.PropertyImage;
import com.jsf.dao.PropertyimageDAO;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import com.jsf.dao.PropertyDAO;
import com.jsf.entities.Property;
import com.jsf.entities.User;

@Named
@ViewScoped
public class PropertyEditBB implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String PAGE_PROPERTY_LIST = "propertyList?faces-redirect=true";
    private static final String PAGE_STAY_AT_THE_SAME = null;
    
    
    private UploadedFile uploadedImage;
    private Property property = new Property();
    private Property loaded = null;

    @EJB
    private PropertyDAO propertyDAO;

    @Inject
    private FacesContext context;

    @Inject
    private Flash flash;
    
    @Inject
    private loginBB loginBB;
    
    @EJB
    private PropertyimageDAO propertyImageDAO;

    public Property getProperty() {
        return property;
    }

    public void onLoad() throws IOException {
        loaded = (Property) flash.get("property");

        if (loaded != null) {
            property = loaded;
        } else {
            property = new Property();
        }
    }

    public String saveData() {
        try {
            if (property == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Brak danych nieruchomości.", null));
                return PAGE_STAY_AT_THE_SAME;
            }
            
         // Walidacja wymaganych pól
            if(property.getAddress() == null || property.getAddress().trim().isEmpty()) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "Adres nieruchomości jest wymagany.", null));
                return PAGE_STAY_AT_THE_SAME;
            }
            if(property.getPrice() == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "Cena nieruchomości jest wymagana.", null));
                return PAGE_STAY_AT_THE_SAME;
            }
            if(property.getType() == null || property.getType().trim().isEmpty()) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "Typ nieruchomości jest wymagany.", null));
                return PAGE_STAY_AT_THE_SAME;
            }
            
            // Pobierz aktualnie zalogowanego użytkownika
            User currentUser = loginBB.getLoggedUser();
            if (currentUser == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Musisz być zalogowany, aby dodać nieruchomość.", null));
                return "login.xhtml?faces-redirect=true";
            }
            // Ustawienie właściciela nieruchomości
            property.setOwnerId(currentUser.getIdUser());
            property.setLastModifiedBy(currentUser.getIdUser());
            property.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
            
            //Dodawanie nowej 
            if (property.getIdProperty() == null) {
                propertyDAO.create(property);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Nieruchomość została dodana.", null));
            } else {
                // Aktualizacja istniejącej
                propertyDAO.merge(property);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Nieruchomość została zaktualizowana.", null));
            }
            
            //Dodawanie zdjęcia
            if (uploadedImage != null && uploadedImage.getSize() > 0) {
                try {
                    // Generowanie unikalnej nazwy pliku
                    String fileName = java.util.UUID.randomUUID().toString() + "_" + uploadedImage.getFileName();

                    // Pierwsza lokalizacja
                    String uploadDir1 = "Y:/kudlacikowe/projekty/jsf_06_web/src/main/webapp/uploads";
                    File dir1 = new File(uploadDir1);
                    if (!dir1.exists()) {
                        dir1.mkdirs();
                    }
                    // Pobranie strumienia wejściowego i zapis do pierwszego katalogu
                    try (InputStream input1 = uploadedImage.getInputStream()) {
                        File file1 = new File(dir1, fileName);
                        Files.copy(input1, file1.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }

                    // Druga lokalizacja
                    String uploadDir2 = "Y:/kudlacikowe/wildfly-34.0.0.Final/standalone/deployments/jsf_06_EAR.ear/jsf_06_web.war/uploads";
                    File dir2 = new File(uploadDir2);
                    if (!dir2.exists()) {
                        dir2.mkdirs();
                    }
                    // Ponowne pobranie strumienia wejściowego i zapis do drugiego katalogu
                    try (InputStream input2 = uploadedImage.getInputStream()) {
                        File file2 = new File(dir2, fileName);
                        Files.copy(input2, file2.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }

                    // Utworzenie i zapis encji PropertyImage z relatywną ścieżką
                    PropertyImage pimg = new PropertyImage();
                    pimg.setProperty(property);
                    pimg.setImagePath("uploads/" + fileName); 
                    propertyImageDAO.create(pimg);

                } catch(IOException e) {
                    e.printStackTrace();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                                    "Błąd podczas zapisu obrazu.", null));
                }
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd podczas zapisywania danych.", null));
            return PAGE_STAY_AT_THE_SAME;
        }

        return PAGE_PROPERTY_LIST;
    }
    
    public UploadedFile getUploadedImage() { 
        return uploadedImage; 
    }
    public void setUploadedImage(UploadedFile uploadedImage) { 
        this.uploadedImage = uploadedImage; 
    }

}
