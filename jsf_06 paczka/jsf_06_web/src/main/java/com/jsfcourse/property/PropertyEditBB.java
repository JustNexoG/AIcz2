package com.jsfcourse.property;

import java.io.IOException;
import java.io.Serializable;

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
    PropertyDAO propertyDAO;

    @Inject
    FacesContext context;

    @Inject
    Flash flash;

    public Property getProperty() {
        return property;
    }

    public void onLoad() throws IOException {
        // Pobranie obiektu Property z flasha
        loaded = (Property) flash.get("property");

        if (loaded != null) {
            property = loaded; // Przypisanie załadowanej nieruchomości
        } else {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błędne użycie systemu", null));
        }
    }

    public String saveData() {
        // Brak obiektu nieruchomości
        if (loaded == null) {
            return PAGE_STAY_AT_THE_SAME;
        }

        try {
            if (property.getIdProperty() == 0) { // Nowa nieruchomość
                propertyDAO.create(property);
            } else { // Aktualizacja istniejącej nieruchomości
                propertyDAO.merge(property);
            }
        } catch (Exception e) {
            e.printStackTrace();
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wystąpił błąd podczas zapisu", null));
            return PAGE_STAY_AT_THE_SAME;
        }

        return PAGE_PROPERTY_LIST;
    }
}
