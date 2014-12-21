package org.nla.alextracks.controller;

import java.text.ParseException;
import java.util.List;

import org.nla.alextracks.dao.IncidentService;
import org.nla.alextracks.model.Incident;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class IncidentController {

	IncidentService incidentService = new IncidentService();

	@RequestMapping(value = "/incidents/{incidentId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public Incident getIncident(@PathVariable int incidentId) {
		Incident incident = incidentService.getIncidentById(incidentId);
		return incident;
	}
	
	@RequestMapping(value = "/incidents", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<Incident> getAllIncidents() {
		List<Incident> incidents = incidentService.getAllIncidents();
		return incidents;
	}

	@RequestMapping(value = "/incidents/insert/{customerId}/{customerAddress}", method = RequestMethod.POST, headers = "Accept=application/json")
	public Incident addIncident(@PathVariable String customerId,
			@PathVariable String customerAddress) throws ParseException {
		Incident incident = new Incident();
		incident.setCustomerId(customerId);
		incident.setCustomerAddress(customerAddress);
		incidentService.addIncident(incident);
		return incident;
	}
}
