package org.nla.alextracks.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.nla.alextracks.model.GeoPoint;
import org.nla.alextracks.model.Incident;
import org.nla.alextracks.utility.DBUtility;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.GeocodingApi.ComponentFilter;
import com.google.maps.PendingResult;
import com.google.maps.PendingResult.Callback;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public class IncidentService {

	private Connection connection;

	private GeoApiContext context;

	public IncidentService() {
		connection = DBUtility.getConnection();
		context = new GeoApiContext().setApiKey("AIzaSyCrU1j1-tmeSNQrD2DgxSI5v5Tp-Imzd6c");
	}

	public void addIncident(Incident incident) {

		
		
		
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("insert into incidents(customer_id, customer_address, latitude, longitude, creation_ts values (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, incident.getCustomerId());
			preparedStatement.setString(2, incident.getCustomerAddress());
			if (incident.getLocation() != null) {
				preparedStatement.setDouble(3, incident.getLocation()
						.getLatitude());
				preparedStatement.setDouble(4, incident.getLocation()
						.getLongitude());
			} else {
				preparedStatement.setNull(3, Types.DOUBLE);
				preparedStatement.setNull(4, Types.DOUBLE);
			}
			preparedStatement.setString(5, "now()");
			preparedStatement.executeUpdate();
			
			ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
			int generatedKey = generatedKeys.getInt(1);
			incident.setId(generatedKey);
		} catch (Exception e) {
			throw new RuntimeException("Unable to insert incident", e);
		}
	}

	public void updateLocation(int incidentId, GeoPoint location)
			throws ParseException {
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("update incidents set latitude=?, longitude=? where customer_id=?");
			preparedStatement.setDouble(1, location.getLatitude());
			preparedStatement.setDouble(2, location.getLongitude());
			preparedStatement.setInt(3, incidentId);
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			throw new RuntimeException("Unable to update incident", e);
		}
	}

	public List<Incident> getAllIncidents() {
		List<Incident> incidents = new ArrayList<Incident>();
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from incidents");
			while (rs.next()) {
				Incident incident = new Incident();
				Date creationTs = rs.getDate("creation_ts");
				incident.setCreationTs(creationTs);
				incident.setCustomerAddress(rs.getString("customer_address"));
				incident.setCustomerId(rs.getString("customer_id"));
				Double latitude = rs.getDouble("latitude");
				if (!rs.wasNull()) {
					Double longitude = rs.getDouble("longitude");
					incident.setLocation(new GeoPoint(latitude, longitude));
				}
				
				
				GeocodingResult[] results = null;
				try {
					results = GeocodingApi.geocode(context, incident.getCustomerAddress()).await();
				} catch (Exception e) {

				}
				
				if (results != null && results[0] != null) {
					GeocodingResult result = results[0];
					incident.setLocation(new GeoPoint(result.geometry.location.lat, result.geometry.location.lng));
					incident.setCorrectedAddress(result.formattedAddress);
				}

				incidents.add(incident);
			}
		} catch (Exception e) {
			throw new RuntimeException("Unable to list incidents", e);
		}

		return incidents;
	}

	public Incident getIncidentById(int incidentId) {
		Incident incident = new Incident();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("select * from incidents where incident_id=?");
			preparedStatement.setInt(1, incidentId);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				Date creationTs = rs.getDate("creation_ts");
				incident.setCreationTs(creationTs);
				incident.setCustomerAddress(rs.getString("customer_address"));
				incident.setCustomerId(rs.getString("customer_id"));
				Double latitude = rs.getDouble("latitude");
				if (!rs.wasNull()) {
					Double longitude = rs.getDouble("longitude");
					incident.setLocation(new GeoPoint(latitude, longitude));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return incident;
	}
}
