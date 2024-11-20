package com.jsfcourse.property;

import java.io.IOException;
import java.io.Serializable;

import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import com.jsf.dao.PropertyDAO;
import com.jsf.entities.Property;

@Named
@ViewScoped
public class PropertyEditGETBB implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String PAGE_PROPERTY_LIST = "propertyList?faces-redirect=true";
    private static final String PAGE_STAY_AT_THE_SAME = null;

    private Property property = new Property();
    private Property loaded = null;

    @Inject
    FacesContext context;

    @EJB
    PropertyDAO propertyDAO;

    public Property getProperty() {
        return property;
    }

    public void onLoad() throws IOException {
        if (!context.isPostback()) {
            if (!context.isValidationFailed() && property.getIdProperty() != null) {
                loaded = propertyDAO.find(property.getIdProperty());
            }
            if (loaded != null) {
                property = loaded;
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nie znaleziono nieruchomości", null));
            }
        }
    }

    public String saveData() {
        // No Property object passed
        if (loaded == null) {
            return PAGE_STAY_AT_THE_SAME;
        }

        try {
            if (property.getIdProperty() == null) {
                // New record
                propertyDAO.create(property);
            } else {
                // Existing record
                propertyDAO.merge(property);
            }
        } catch (Exception e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wystąpił błąd podczas zapisu", null));
            return PAGE_STAY_AT_THE_SAME;
        }

        return PAGE_PROPERTY_LIST;
    }
}
