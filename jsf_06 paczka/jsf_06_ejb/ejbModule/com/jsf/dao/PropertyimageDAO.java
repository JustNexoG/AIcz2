package com.jsf.dao;

import com.jsf.entities.PropertyImage;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class PropertyimageDAO {
    @PersistenceContext(unitName = "jsfcourse-simplePU")
    private EntityManager em;

    public void create(PropertyImage propertyImage) {
        em.persist(propertyImage);
    }
    
}
