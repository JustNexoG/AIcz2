package com.jsf.dao;

import com.jsf.entities.Role;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class RoleDAO {

    @PersistenceContext
    private EntityManager em;

    public Role find(int idRole) {
        return em.find(Role.class, idRole);
    }
}