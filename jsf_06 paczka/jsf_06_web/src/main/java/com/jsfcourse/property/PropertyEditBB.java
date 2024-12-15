package com.jsfcourse.property;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import com.jsf.dao.PropertyDAO;
import com.jsf.entities.Property;

@Named
@ViewScoped
public class PropertyEditBB implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String PAGE_PROPERTY_LIST = "propertyList?faces-redirect=true";
    private static final String PAGE_STAY_AT_THE_SAME = null;

    private Property property = new Property();
    private Property loaded = null;

    @EJB
    private PropertyDAO propertyDAO;

    @Inject
    private FacesContext context;

    @Inject
    private Flash flash;

    public Property getProperty() {
        return property;
    }

    public void onLoad() throws IOException {
        loaded = (Property) flash.get("property");

        if (loaded != null) {
            property = loaded;
        } else {
            property = new Property(); // Inicjalizacja dla nowej nieruchomości
        }
    }

    public String saveData() {
        try {
            if (property == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Brak danych nieruchomości.", null));
                return PAGE_STAY_AT_THE_SAME;
            }

            // Obsługa tworzenia nowej nieruchomości
            if (property.getIdProperty() == null) {
                propertyDAO.create(property);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Nieruchomość została dodana.", null));
            } else {
                // Aktualizacja istniejącej nieruchomości
                propertyDAO.merge(property);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Nieruchomość została zaktualizowana.", null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd podczas zapisywania danych.", null));
            return PAGE_STAY_AT_THE_SAME;
        }

        return PAGE_PROPERTY_LIST;
    }

}
