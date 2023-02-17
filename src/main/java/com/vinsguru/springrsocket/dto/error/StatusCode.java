package com.vinsguru.springrsocket.dto.error;

public enum StatusCode {

	EC001("given number is not in range"),
	EC002("your ussage limit exceeded");
	
	private final String description;
	
	private StatusCode(final String description)
	{
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
