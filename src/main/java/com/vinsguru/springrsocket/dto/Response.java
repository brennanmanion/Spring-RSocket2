package com.vinsguru.springrsocket.dto;

import java.util.Objects;

import com.vinsguru.springrsocket.dto.error.ErrorEvent;

public class Response<T> {

	ErrorEvent errorResponse;
	T successResponse;
	
	public Response() {
	}
	
	public Response(final ErrorEvent errorResponse)
	{
		this.errorResponse = errorResponse;
	}
	
	public Response(final T successResponse)
	{
		this.successResponse = successResponse;
	}

	public boolean hasError()
	{
		return Objects.nonNull(this.errorResponse);
	}
	
	public ErrorEvent getErrorResponse() {
		return errorResponse;
	}

	public T getSuccessResponse() {
		return successResponse;
	}
	
	public static <T> Response<T> with(T t)
	{
		return new Response<T>(t);
	}
	
	public static <T> Response<T> with(ErrorEvent errorResponse)
	{
		return new Response<T>(errorResponse);
	}
	
}
