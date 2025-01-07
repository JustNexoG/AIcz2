package com.jsf.dao;

import com.jsf.entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

@Stateless
public class UserDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(User user) {
        try {
            em.persist(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void flush() {
        try {
            em.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public User merge(User user) {
        try {
            User mergedUser = em.merge(user);
            return mergedUser;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void remove(User user) {
        try {
            em.remove(em.merge(user));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

   
    public boolean isEmailUsed(String email) {

        Long count = em.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
            .setParameter("email", email)
            .getSingleResult();

        // Jeśli count > 0, to email jest używany
        boolean used = (count != null && count > 0L);
        return used;
    }
    
    public User findByEmail(String email) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                     .setParameter("email", email)
                     .getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

}
