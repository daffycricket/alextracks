package org.nla.alextracks.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.nla.alextracks.model.AddressNotFoundException;
import org.nla.alextracks.model.GeoPoint;
import org.nla.alextracks.model.Incident;
import org.nla.alextracks.model.IncidentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

public class IncidentDao {

	@Autowired
    private DataSource dataSource;
	
	@Autowired
	private GeoApiContext geoApiContext;
	
	public Incident addIncident(Incident incident) {
		enrichIncidentWithGeoInfo(incident);
		
		try {
			PreparedStatement preparedStatement = dataSource.getConnection()
					.prepareStatement("insert into incidents(customer_id, customer_address, creation_ts, latitude, longitude, formatted_address) values (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, incident.getCustomerId());
			preparedStatement.setString(2, incident.getCustomerAddress());
			preparedStatement.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()) );
			
			if (incident.getLocation() != null) {
				preparedStatement.setDouble(4, incident.getLocation()
						.getLatitude());
				preparedStatement.setDouble(5, incident.getLocation()
						.getLongitude());
				preparedStatement.setString(6, incident.getFormattedAddress());
			} else {
				preparedStatement.setNull(4, Types.DOUBLE);
				preparedStatement.setNull(5, Types.DOUBLE);
				preparedStatement.setNull(6, Types.VARCHAR);
			}
			
			preparedStatement.executeUpdate();
			
			ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
			generatedKeys.first();
			int generatedKey = generatedKeys.getInt(1);
			incident.setId(generatedKey);
		} catch (Exception e) {
			throw new RuntimeException("Unable to insert incident", e);
		}
		return incident;
	}

	public List<Incident> getAllIncidents() {
		List<Incident> incidents = new ArrayList<Incident>();
		try {
			Statement statement = dataSource.getConnection().createStatement();
			ResultSet rs = statement.executeQuery("select * from incidents order by creation_ts desc");
			while (rs.next()) {
				Incident incident = new Incident();
				Date creationTs = rs.getDate("creation_ts");
				incident.setCreationTs(creationTs);
				incident.setCustomerAddress(rs.getString("customer_address"));
				incident.setCustomerId(rs.getString("customer_id"));
				enrichIncidentWithGeoInfo(incident);
				incidents.add(incident);
			}
		} catch (Exception e) {
			throw new RuntimeException("Unable to list incidents", e);
		}

		return incidents;
	}

	public Incident getIncidentById(int incidentId) {
		Incident incident = null;
		try {
			PreparedStatement preparedStatement = dataSource.getConnection()
					.prepareStatement("select * from incidents where incident_id=?");
			preparedStatement.setInt(1, incidentId);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				incident = new Incident();
				Date creationTs = rs.getDate("creation_ts");
				incident.setCreationTs(creationTs);
				incident.setCustomerAddress(rs.getString("customer_address"));
				incident.setCustomerId(rs.getString("customer_id"));
				incident.setId(rs.getInt("incident_id"));
				enrichIncidentWithGeoInfo(incident);
			}
		} catch (Exception e) {
			throw new RuntimeException("Unable to retrieve incident " + incidentId, e);
		}
		
		if (incident == null) {
			throw new IncidentNotFoundException(incidentId);
		}

		return incident;
	}
	

	public int removeAllIncidents() {
		int removedIncidentCount = 0;
		try {
			Statement statement = dataSource.getConnection().createStatement();
			removedIncidentCount = statement.executeUpdate("delete from incidents");
		} catch (Exception e) {
			throw new RuntimeException("Unable to remove incidents", e);
		}
		return removedIncidentCount;
	}
	
	public int removeIncidentById(int incidentId) {
		int removedIncidentCount = 0;
		try {
			PreparedStatement preparedStatement = dataSource.getConnection()
					.prepareStatement("delete from incidents where incident_id=?");
			preparedStatement.setInt(1, incidentId);
			removedIncidentCount = preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException("Unable to remove incident " + incidentId, e);
		}
		
		if (removedIncidentCount == 0) {
			throw new IncidentNotFoundException(incidentId);
		}
		
		return removedIncidentCount;
	}
	
	private void enrichIncidentWithGeoInfo(Incident incident) {
		GeocodingResult[] results = null;
		try {
			results = GeocodingApi.geocode(geoApiContext, incident.getCustomerAddress()).await();
		} catch (Exception e) {

		}
		
		if (results != null && results.length > 0 && results[0] != null) {
			GeocodingResult result = results[0];
			GeoPoint location = new GeoPoint(result.geometry.location.lat, result.geometry.location.lng);
			incident.setLocation(location);
			incident.setFormattedAddress(result.formattedAddress);
		} else {
			throw new AddressNotFoundException(incident.getCustomerAddress());
		}
	}
}
