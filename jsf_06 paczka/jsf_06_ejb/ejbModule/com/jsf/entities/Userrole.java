package com.jsf.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import jakarta.persistence.*;

@Entity
@Table(name = "userrole")
public class Userrole implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UserrolePK id; // zawiera idUser i idRole

	@Column(name = "assignedDate")
	private Timestamp assignedDate;

	// -- Relacja do tabeli 'roles' poprzez pole 'idRole' w PK --
	@ManyToOne
	@MapsId("idRole") // mówimy: użyj idRole z PK
	@JoinColumn(name = "idRole", nullable = false) 
	private Role role;

	// -- Relacja do tabeli 'users' poprzez pole 'idUser' w PK --
	@ManyToOne
	@MapsId("idUser") // mówimy: użyj idUser z PK
	@JoinColumn(name = "idUser", nullable = false)
	private User user;

	public Userrole() {
	}

	public UserrolePK getId() {
		return id;
	}

	public void setId(UserrolePK id) {
		this.id = id;
	}

	public Timestamp getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(Timestamp assignedDate) {
		this.assignedDate = assignedDate;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Userrole [id=" + id + ", assignedDate=" + assignedDate 
				+ ", role=" + (role != null ? role.getIdRole() : null)
				+ ", user=" + (user != null ? user.getIdUser() : null) + "]";
	}
}
