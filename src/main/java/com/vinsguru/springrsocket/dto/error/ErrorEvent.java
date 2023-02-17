package com.vinsguru.springrsocket.dto.error;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ErrorEvent {

	private StatusCode statusCode;
	private final LocalDate date = LocalDate.now();
	
}
