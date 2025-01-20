package com.jsfcourse.property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import com.jsf.dao.PropertyDAO;
import com.jsf.entities.Property;
import com.jsfcourse.property.loginBB;

@Named
@RequestScoped
public class MyPropertiesBB implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Property> myProperties;

    @EJB
    private PropertyDAO propertyDAO;

    @Inject
    private loginBB loginBB;

    @PostConstruct
    public void init() {
        if (loginBB.getLoggedUser() != null) {
            myProperties = propertyDAO.getPropertiesByOwner(loginBB.getLoggedUser().getIdUser());
        } else {
            myProperties = new ArrayList<>();
        }
    }

    public List<Property> getMyProperties() {
        return myProperties;
    }
}
