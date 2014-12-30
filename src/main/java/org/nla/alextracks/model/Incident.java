package org.nla.alextracks.model;

import java.util.Date;

public class Incident {

	private int id;

	private String customerId;

	private String customerAddress;

	private String formattedAddress;

	private GeoPoint location;

	private Date creationTs;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public GeoPoint getLocation() {
		return location;
	}

	public void setLocation(GeoPoint location) {
		this.location = location;
	}

	public Date getCreationTs() {
		return creationTs;
	}

	public void setCreationTs(Date creationTs) {
		this.creationTs = creationTs;
	}

	public String getFormattedAddress() {
		return formattedAddress;
	}

	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}

	@Override
	public String toString() {
		return "Incident [id=" + id + ", customerId=" + customerId
				+ ", customerAddress=" + customerAddress
				+ ", formattedAddress=" + formattedAddress + ", location="
				+ location + ", creationTs=" + creationTs + "]";
	}
}