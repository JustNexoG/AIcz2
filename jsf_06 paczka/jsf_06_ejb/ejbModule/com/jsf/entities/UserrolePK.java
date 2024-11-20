package com.jsf.entities;

import java.io.Serializable;
import jakarta.persistence.*;

/**
 * The primary key class for the userrole database table.
 * 
 */
@Embeddable
public class UserrolePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(insertable=false, updatable=false)
	private int idUser;

	@Column(insertable=false, updatable=false)
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

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UserrolePK)) {
			return false;
		}
		UserrolePK castOther = (UserrolePK)other;
		return 
			(this.idUser == castOther.idUser)
			&& (this.idRole == castOther.idRole);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idUser;
		hash = hash * prime + this.idRole;
		
		return hash;
	}
}