package com.jsfcourse.property;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ejb.EJB;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.Flash;
import jakarta.annotation.PostConstruct;
import com.jsf.dao.PropertyDAO;
import com.jsf.entities.Property;

@Named
@RequestScoped
public class PropertyListBB {
    private static final String PAGE_PROPERTY_EDIT = "propertyEdit?faces-redirect=true";
    private static final String PAGE_STAY_AT_THE_SAME = null;

    private String address;

    @Inject
    ExternalContext extcontext;

    @Inject
    Flash flash;

    @EJB
    PropertyDAO propertyDAO;
    
    private LazyDataModel<Property> lazyModel;
    private Property selectedProperty;

    @PostConstruct
    public void init() {
        lazyModel = new LazyPropertyDataModel(propertyDAO);
    }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public LazyDataModel<Property> getLazyModel() { return lazyModel; }
    
    public Property getSelectedProperty() {
        return selectedProperty;
    }

    public void setSelectedProperty(Property selectedProperty) {
        this.selectedProperty = selectedProperty;
    }
    
    public List<Property> getFullList() {
        return propertyDAO.getFullList();
    }

    public List<Property> getList() {
        List<Property> list = null;

        Map<String, Object> searchParams = new HashMap<>();

        if (address != null && address.length() > 0) {
            searchParams.put("address", address);
        }

        list = propertyDAO.getList(searchParams);

        return list;
    }

    public String newProperty() {
        Property property = new Property();

        flash.put("property", property);

        return PAGE_PROPERTY_EDIT;
    }

    public String editProperty(Property property) {
        flash.put("property", property);

        return PAGE_PROPERTY_EDIT;
    }

    public String deleteProperty(Property property) {
        propertyDAO.remove(property);
        return PAGE_STAY_AT_THE_SAME;
    }
}
