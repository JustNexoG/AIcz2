package com.jsf.entities;

import java.io.Serializable;
import jakarta.persistence.*;

@Embeddable
public class UserrolePK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "idUser")
	private int idUser;

	@Column(name = "idRole")
	private int idRole;

	public UserrolePK() {
	}

	public int getIdUser() {
		return this.idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public int getIdRole() {
		return this.idRole;
	}

	public void setIdRole(int idRole) {
		this.idRole = idRole;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UserrolePK)) {
			return false;
		}
		UserrolePK castOther = (UserrolePK) other;
		return (this.idUser == castOther.idUser) 
			&& (this.idRole == castOther.idRole);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idUser;
		hash = hash * prime + this.idRole;
		return hash;
	}

	@Override
	public String toString() {
		return "UserrolePK [idUser=" + idUser + ", idRole=" + idRole + "]";
	}
}
