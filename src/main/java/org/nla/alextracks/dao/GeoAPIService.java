package org.nla.alextracks.dao;

import org.nla.alextracks.model.GeoPoint;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

public class GeoAPIService {

	@Autowired
	private GeoApiContext geoApiContext;
	
	public GeoPoint getLocation(String address) {
		GeoPoint geoPoint = null;
		try {
			GeocodingResult[] results = null;
			try {
				results = GeocodingApi.geocode(geoApiContext, address).await();
			} catch (Exception e) {

			}
			
			if (results != null && results[0] != null) {
				GeocodingResult result = results[0];
				geoPoint = new GeoPoint(result.geometry.location.lat, result.geometry.location.lng);
			}
		} catch (Exception e) {
			throw new RuntimeException("Unable to insert incident", e);
		}
		return geoPoint;
	}
}
