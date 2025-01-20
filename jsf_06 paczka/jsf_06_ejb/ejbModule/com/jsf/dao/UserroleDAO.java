package com.jsf.dao;

import java.util.List;

import com.jsf.entities.Role;
import com.jsf.entities.Userrole;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class UserroleDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(Userrole userrole) {
        em.merge(userrole);
    }
    
    public List<Role> getRolesForUser(int idUser) {
        String jpql = "SELECT ur.role FROM Userrole ur WHERE ur.user.idUser = :idUser";
        return em.createQuery(jpql, Role.class)
                 .setParameter("idUser", idUser)
                 .getResultList();
    }
    
    public boolean userHasRole(int userId, int roleId) {
        String jpql = "SELECT COUNT(ur) FROM Userrole ur WHERE ur.user.idUser = :uid AND ur.role.idRole = :rid";
        Long count = em.createQuery(jpql, Long.class)
                       .setParameter("uid", userId)
                       .setParameter("rid", roleId)
                       .getSingleResult();
        return count != null && count > 0;
    }

}
