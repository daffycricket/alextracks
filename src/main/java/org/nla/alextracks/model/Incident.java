package org.nla.alextracks.model;

import java.util.Date;

public class Incident {

	private int id;

	private String customerId;

	private String customerAddress;

	private String correctedAddress;

	public String getCorrectedAddress() {
		return correctedAddress;
	}

	public void setCorrectedAddress(String correctedAddress) {
		this.correctedAddress = correctedAddress;
	}

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

	@Override
	public String toString() {
		return "Incident [id=" + id + ", customerId=" + customerId
				+ ", customerAddress=" + customerAddress + ", location="
				+ location + ", creationTs=" + creationTs + "]";
	}
}