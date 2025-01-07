package com.jsfcourse.property;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import java.io.Serializable;
import java.util.List;

import com.jsf.dao.UserroleDAO;
import com.jsf.entities.Role;
import com.jsfcourse.property.loginBB;
import com.jsf.entities.User;

@Named
@RequestScoped
public class roleBB implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private loginBB loginBB;

    @Inject
    private UserroleDAO userroleDAO;

    public String getUserRoles() {
        User user = loginBB.getLoggedUser();
        if (user == null) {
            return "";
        }
        List<Role> roles = userroleDAO.getRolesForUser(user.getIdUser());
        if (roles == null || roles.isEmpty()) {
            return "";
        }
        //Łączy nazwy ról przecinkiem
        return String.join(", ", roles.stream().map(Role::getRoleName).toArray(String[]::new));
    }
}
