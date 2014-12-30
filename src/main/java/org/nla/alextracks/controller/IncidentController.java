package org.nla.alextracks.controller;

import java.text.ParseException;
import java.util.List;

import org.nla.alextracks.dao.IncidentDao;
import org.nla.alextracks.model.Incident;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class IncidentController {

	private IncidentDao incidentDao;
	
	@Autowired
	public IncidentController(IncidentDao incidentDao) {
		this.incidentDao = incidentDao;
	}

	@RequestMapping(value = "/incidents/insert/{customerId}/{customerAddress}", method = RequestMethod.POST, headers = "Accept=application/json")
	public Incident addIncident(@PathVariable String customerId,
			@PathVariable String customerAddress) throws ParseException {
		Incident incident = new Incident();
		incident.setCustomerId(customerId);
		incident.setCustomerAddress(customerAddress);
		incidentDao.addIncident(incident);
		return incident;
	}
	
	@RequestMapping(value = "/incidents/{incidentId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public Incident getIncident(@PathVariable int incidentId) {
		Incident incident = incidentDao.getIncidentById(incidentId);
		return incident;
	}
	
	@RequestMapping(value = "/incidents", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<Incident> getAllIncidents() {
		List<Incident> incidents = incidentDao.getAllIncidents();
		return incidents;
	}
	
	@RequestMapping(value = "/incidents/{incidentId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public int removeIncident(@PathVariable int incidentId) {
		return incidentDao.removeIncidentById(incidentId);
	}
	
	@RequestMapping(value = "/incidents", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public int removeAllIncidents() {
		return incidentDao.removeAllIncidents();
	}
}