package org.nla.alextracks.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AddressNotFoundException extends RuntimeException {
	
	public AddressNotFoundException(String address) {
		super(address);
	}
	
}