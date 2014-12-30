package org.nla.alextracks.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;





import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;





import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.nla.alextracks.dao.IncidentDao;
import org.nla.alextracks.model.GeoPoint;
import org.nla.alextracks.model.Incident;
import org.nla.alextracks.model.IncidentNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;





import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;





import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IncidentControllerUnitTest {

	@Mock
	private IncidentDao incidentDao;

	@InjectMocks
	private IncidentController incidentController;

	private MockMvc mockMvc;

	private ObjectMapper jsonObjectMapper;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(incidentController)
				.build();
		this.jsonObjectMapper = new ObjectMapper();
	}

	@Test
	@Ignore
	public void deserializeOneIncident() throws Exception {
		String jsonIncident = "{\"id\":5,\"customerId\":\"customer1\",\"customerAddress\":\"13, rue Charles d'Orleans 16100 cognac\",\"formattedAddress\":\"My formatted address\",\"location\":{\"latitude\":1.0,\"longitude\":2.0},\"creationTs\":null}";
		Incident incident = jsonObjectMapper.readValue(jsonIncident,
				Incident.class);
		assertThat(incident, is(notNullValue()));
	}

	@Test
	public void addIncident_OK() throws Exception {
		when(incidentDao.addIncident(Mockito.any(Incident.class)))
				.thenAnswer(
						(invocation) -> {
							Incident incident = (Incident) invocation
									.getArguments()[0];
							incident.setId(5);
							incident.setCustomerAddress("79, rue marcel dassault 92100 boulogne-billancourt");
							incident.setFormattedAddress("My formatted address");
							incident.setLocation(new GeoPoint(1.0, 2.0));
							incident.setCreationTs(new Date());
							return incident;
						});

		MvcResult mvcResult = this.mockMvc
				.perform(
						post("/rest/incidents/insert/{customerId}/{customerAddress}",
								"customer1",
								"13, rue Charles d'Orleans 16100 cognac"))
				.andExpect(status().isOk()).andReturn();

		String jsonResult = mvcResult.getResponse().getContentAsString();
		Incident incident = jsonObjectMapper.readValue(jsonResult,
				Incident.class);
		
		assertThat(incident, notNullValue());
		assertThat(incident.getFormattedAddress(), notNullValue());
		assertThat(incident.getFormattedAddress(), equalTo("My formatted address"));
		assertThat(incident.getLocation(), notNullValue());
		assertThat(incident.getLocation().getLatitude(), equalTo(1.0));
		assertThat(incident.getLocation().getLongitude(), equalTo(2.0));
	}

	@Test
	public void getIncident_OK() throws Exception {
		when(incidentDao.getIncidentById(Mockito.anyInt())).thenAnswer(
				(invocation) -> {
					int incidentId = (Integer) invocation.getArguments()[0];
					Incident incident = new Incident();
					incident.setId(incidentId);
					return incident;
				});

		MvcResult mvcResult = this.mockMvc
				.perform(get("/rest/incidents/{incidentId}", 1))
				.andExpect(status().isOk()).andReturn();

		String jsonResult = mvcResult.getResponse().getContentAsString();
		Incident incident = jsonObjectMapper.readValue(jsonResult,
				Incident.class);
		assertThat(incident, is(notNullValue()));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void getIncident_KO_IncidentDontExist() throws Exception {
		when(incidentDao.getIncidentById(Mockito.anyInt())).thenThrow(IncidentNotFoundException.class);
		this.mockMvc
				.perform(get("/rest/incidents/{incidentId}", 1))
				.andExpect(status().is(404));
	}

	@Test
	public void getAllIncidents_OK() throws Exception {
		when(incidentDao.getAllIncidents())
				.thenAnswer(
						(invocation) -> {
							Incident incident1 = new Incident();
							incident1.setId(1);
							incident1.setCustomerId("customer1");
							incident1
									.setCustomerAddress("79, rue marcel dassault 92100 boulogne-billancourt");
							incident1.setFormattedAddress("My formatted address 1");
							incident1.setLocation(new GeoPoint(1.0, 2.0));

							incident1.setCreationTs(new Date());
							Incident incident2 = new Incident();
							incident2.setId(2);
							incident2.setCustomerId("customer2");
							incident2
									.setCustomerAddress("this is my address in Paris");
							incident2.setFormattedAddress("My formatted address 2");
							incident2.setLocation(new GeoPoint(1.0, 2.0));

							incident2.setCreationTs(new Date());

							return Arrays.asList(incident1, incident2);
						});

		MvcResult mvcResult = this.mockMvc.perform(get("/rest/incidents"))
				.andExpect(status().isOk()).andReturn();

		String jsonResult = mvcResult.getResponse().getContentAsString();
		List<Incident> incidents = jsonObjectMapper.readValue(jsonResult,
				new TypeReference<List<Incident>>() {
				});

		assertThat(incidents, not(is(nullValue())));
		assertThat(incidents.size(), equalTo(2));
	}
	
	@Test
	public void removeAllIncidents_OK() throws Exception {
		when(incidentDao.removeAllIncidents()).thenReturn(5);
		this.mockMvc.perform(delete("/rest/incidents")).andExpect(status().isOk());
	}
	
	@Test
	public void removeIncident_OK() throws Exception {
		when(incidentDao.removeIncidentById(Mockito.anyInt())).thenReturn(1);
		this.mockMvc
				.perform(delete("/rest/incidents/{incidentId}", 1))
				.andExpect(status().isOk());
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void removeIncident_KO_IncidentDontExist() throws Exception {
		when(incidentDao.removeIncidentById(Mockito.anyInt())).thenThrow(IncidentNotFoundException.class);
		this.mockMvc
				.perform(delete("/rest/incidents/{incidentId}", 1))
				.andExpect(status().is(404));
	}
}
