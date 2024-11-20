package com.jsf.entities;

import java.io.Serializable;
import jakarta.persistence.*;


/**
 * The persistent class for the property_images database table.
 * 
 */
@Entity
@Table(name="property_images")
@NamedQuery(name="PropertyImage.findAll", query="SELECT p FROM PropertyImage p")
public class PropertyImage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idImage;

	private String imagePath;

	//bi-directional many-to-one association to Property
	@ManyToOne
	@JoinColumn(name="property_id")
	private Property property;

	public PropertyImage() {
	}

	public int getIdImage() {
		return this.idImage;
	}

	public void setIdImage(int idImage) {
		this.idImage = idImage;
	}

	public String getImagePath() {
		return this.imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Property getProperty() {
		return this.property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

}