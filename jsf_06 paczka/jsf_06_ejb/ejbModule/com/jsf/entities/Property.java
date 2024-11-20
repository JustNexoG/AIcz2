package com.jsf.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the properties database table.
 * 
 */
@Entity
@Table(name="properties")
@NamedQuery(name="Property.findAll", query="SELECT p FROM Property p")
public class Property implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idProperty;

	private String address;

	private Timestamp datePosted;

	private String description;

	private Integer lastModifiedBy;

	private Timestamp lastModifiedDate;

	private int ownerId;

	private BigDecimal price;

	private String type;

	//bi-directional many-to-many association to User
	@ManyToMany(mappedBy="properties")
	private List<User> users;

	//bi-directional many-to-one association to PropertyImage
	@OneToMany(mappedBy="property")
	private List<PropertyImage> propertyImages;

	public Property() {
	}

	public Integer getIdProperty() {
		return this.idProperty;
	}

	public void setIdProperty(Integer idProperty) {
		this.idProperty = idProperty;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Timestamp getDatePosted() {
		return this.datePosted;
	}

	public void setDatePosted(Timestamp datePosted) {
		this.datePosted = datePosted;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(Integer lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public int getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<PropertyImage> getPropertyImages() {
		return this.propertyImages;
	}

	public void setPropertyImages(List<PropertyImage> propertyImages) {
		this.propertyImages = propertyImages;
	}

	public PropertyImage addPropertyImage(PropertyImage propertyImage) {
		getPropertyImages().add(propertyImage);
		propertyImage.setProperty(this);

		return propertyImage;
	}

	public PropertyImage removePropertyImage(PropertyImage propertyImage) {
		getPropertyImages().remove(propertyImage);
		propertyImage.setProperty(null);

		return propertyImage;
	}

}