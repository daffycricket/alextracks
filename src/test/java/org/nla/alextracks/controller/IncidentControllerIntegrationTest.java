package org.nla.alextracks.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.nla.alextracks.model.Incident;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration
(
  {
   "file:src/main/webapp/WEB-INF/rest-servlet.xml"
  }
)
public class IncidentControllerIntegrationTest {

	@Autowired
    private WebApplicationContext wac;

	private MockMvc mockMvc;

	private ObjectMapper jsonObjectMapper;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		this.jsonObjectMapper = new ObjectMapper();
		this.mockMvc.perform(delete("/rest/incidents"));
	}
	
	@After
	public void tearDown() throws Exception {
		this.mockMvc.perform(delete("/rest/incidents"));
	}
	
	@Test
	public void addIncident_OK() throws Exception {
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
		assertThat(incident.getLocation(), notNullValue());
		assertThat(incident.getLocation().getLatitude(), notNullValue());
		assertThat(incident.getLocation().getLongitude(), notNullValue());
	}
	
	@Test
	public void addIncident_K0_AddressDontExist() throws Exception {
		this.mockMvc
				.perform(
						post("/rest/incidents/insert/{customerId}/{customerAddress}",
								"customer1",
								"fdfjfdkgjgfjksghjgdklwg"))
				.andExpect(status().is(400));
	}

	@Test
	public void getIncident_OK() throws Exception {
		
		MvcResult mvcResultCreateIncident = this.mockMvc
		.perform(
				post("/rest/incidents/insert/{customerId}/{customerAddress}",
						"customer1",
						"13, rue Charles d'Orleans 16100 cognac")).andReturn();
		
		String jsonResultCreateIncident = mvcResultCreateIncident.getResponse().getContentAsString();
		Incident storedIncident = jsonObjectMapper.readValue(jsonResultCreateIncident,
				Incident.class);
		
		MvcResult mvcResult = this.mockMvc
				.perform(get("/rest/incidents/{incidentId}", storedIncident.getId()))
				.andExpect(status().isOk()).andReturn();

		String jsonResult = mvcResult.getResponse().getContentAsString();
		Incident incident = jsonObjectMapper.readValue(jsonResult,
				Incident.class);
		assertThat(incident, is(notNullValue()));
	}
	
	@Test
	public void getIncident_KO_IncidentDontExist() throws Exception {
		this.mockMvc
				.perform(get("/rest/incidents/{incidentId}", 17373984))
				.andExpect(status().is(404));
	}

	@Test
	public void getAllIncidents_OK() throws Exception {
		this.mockMvc
		.perform(
				post("/rest/incidents/insert/{customerId}/{customerAddress}",
						"customer1",
						"13, rue Charles d'Orleans 16100 cognac"));
		this.mockMvc
		.perform(
				post("/rest/incidents/insert/{customerId}/{customerAddress}",
						"customer1",
						"13, rue Charles d'Orleans 16100 cognac"));
		
		MvcResult mvcResult = this.mockMvc.perform(get("/rest/incidents"))
				.andExpect(status().isOk()).andReturn();

		String jsonResult = mvcResult.getResponse().getContentAsString();
		List<Incident> incidents = jsonObjectMapper.readValue(jsonResult,
				new TypeReference<List<Incident>>() {
				});

		assertThat(incidents, not(is(nullValue())));
		assertThat(incidents.size(), greaterThanOrEqualTo(2));
	}
	
	@Test
	public void removeAllIncidents_OK() throws Exception {
		this.mockMvc
		.perform(
				post("/rest/incidents/insert/{customerId}/{customerAddress}",
						"customer1",
						"13, rue Charles d'Orleans 16100 cognac"));
		this.mockMvc
		.perform(
				post("/rest/incidents/insert/{customerId}/{customerAddress}",
						"customer1",
						"13, rue Charles d'Orleans 16100 cognac"));
		
		this.mockMvc.perform(delete("/rest/incidents"))
		.andExpect(status().isOk())
		.andExpect(content().string("2"))
		;
	}
	
	@Test
	public void removeIncident_OK() throws Exception {
		MvcResult mvcResult = this.mockMvc
		.perform(
				post("/rest/incidents/insert/{customerId}/{customerAddress}",
						"customer1",
						"13, rue Charles d'Orleans 16100 cognac")).andReturn();
		
		String jsonResult = mvcResult.getResponse().getContentAsString();
		Incident incident = jsonObjectMapper.readValue(jsonResult,
				Incident.class);
		
		this.mockMvc
				.perform(delete("/rest/incidents/{incidentId}", incident.getId()))
				.andExpect(status().isOk());
	}
	
	@Test
	public void removeIncident_KO_IncidentDontExist() throws Exception {
		this.mockMvc
				.perform(delete("/rest/incidents/{incidentId}", 444))
				.andExpect(status().is(404));
	}
}
