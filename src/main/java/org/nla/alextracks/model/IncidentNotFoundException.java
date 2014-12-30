package org.nla.alextracks.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class IncidentNotFoundException extends RuntimeException {

	public IncidentNotFoundException(int incidentId) {
		super(Integer.toString(incidentId));
	}
}
