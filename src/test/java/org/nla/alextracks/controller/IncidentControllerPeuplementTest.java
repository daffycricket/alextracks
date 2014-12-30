package org.nla.alextracks.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration
(
  {
   "file:src/main/webapp/WEB-INF/rest-servlet.xml"
  }
)
public class IncidentControllerPeuplementTest {

	@Autowired
    private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		this.mockMvc.perform(delete("/rest/incidents"));
	}
		
	@Test
	public void createIncidents_OK() throws Exception {
		this.mockMvc
		.perform(
				post("/rest/incidents/insert/{customerId}/{customerAddress}",
						"customer1",
						"19, rue marcel dassault 92100 boulogne billancourt"));
		this.mockMvc
		.perform(
				post("/rest/incidents/insert/{customerId}/{customerAddress}",
						"customer2",
						"2, rue de l'universite 75007 paris"));
		
		this.mockMvc
		.perform(
				post("/rest/incidents/insert/{customerId}/{customerAddress}",
						"customer3",
						"17 Boulevard de Magenta Paris 75010"));
		
		this.mockMvc
		.perform(
				post("/rest/incidents/insert/{customerId}/{customerAddress}",
						"customer4",
						"11 rue de cambrai Paris 75019"));
	}
}
