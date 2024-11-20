package com.jsf.dao;

import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import com.jsf.entities.Property;

@Stateless
public class PropertyDAO {
    private final static String UNIT_NAME = "jsfcourse-simplePU";

    @PersistenceContext(unitName = UNIT_NAME)
    protected EntityManager em;

    public void create(Property property) {
        em.persist(property);
    }

    public Property merge(Property property) {
        return em.merge(property);
    }

    public void remove(Property property) {
        em.remove(em.merge(property));
    }

    public Property find(Object id) {
        return em.find(Property.class, id);
    }

    public List<Property> getFullList() {
        List<Property> list = null;

        Query query = em.createQuery("SELECT p FROM Property p");

        try {
            list = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Property> getList(Map<String, Object> searchParams) {
        List<Property> list = null;

        String select = "SELECT p ";
        String from = "FROM Property p ";
        String where = "";
        String orderby = "ORDER BY p.price ASC, p.address";

        // Search by address
        String address = (String) searchParams.get("address");
        if (address != null) {
            if (where.isEmpty()) {
                where = "WHERE ";
            } else {
                where += "AND ";
            }
            where += "p.address LIKE :address ";
        }

        // Search by price range
        Double minPrice = (Double) searchParams.get("minPrice");
        if (minPrice != null) {
            if (where.isEmpty()) {
                where = "WHERE ";
            } else {
                where += "AND ";
            }
            where += "p.price >= :minPrice ";
        }

        Double maxPrice = (Double) searchParams.get("maxPrice");
        if (maxPrice != null) {
            if (where.isEmpty()) {
                where = "WHERE ";
            } else {
                where += "AND ";
            }
            where += "p.price <= :maxPrice ";
        }

        Query query = em.createQuery(select + from + where + orderby);

        if (address != null) {
            query.setParameter("address", address + "%");
        }
        if (minPrice != null) {
            query.setParameter("minPrice", minPrice);
        }
        if (maxPrice != null) {
            query.setParameter("maxPrice", maxPrice);
        }

        try {
            list = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
