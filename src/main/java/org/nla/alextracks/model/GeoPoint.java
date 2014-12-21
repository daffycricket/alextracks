package org.nla.alextracks.model;

public class GeoPoint {

	private double latitude;
	
	private double longitude;

	public GeoPoint(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return "GeoPoint [latitude=" + latitude + ", longitude=" + longitude
				+ "]";
	}
}
