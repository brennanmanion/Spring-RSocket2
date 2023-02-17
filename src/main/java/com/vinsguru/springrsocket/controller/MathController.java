package com.vinsguru.springrsocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.vinsguru.springrsocket.dto.ChartResponseDto;
import com.vinsguru.springrsocket.dto.ComputationRequestDto;
import com.vinsguru.springrsocket.dto.ComputationResponseDto;
import com.vinsguru.springrsocket.service.MathService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class MathController {

	@Autowired
	private MathService service;
	
	@MessageMapping("math.service.print")
//	public Mono<Void> print(Mono<ComputationRequestDto> requestDtoMono)
	public Mono<Void> print(Mono<String> request)
	{
//		return this.service.print(requestDtoMono);
		return this.service.print(request);
	}
	
	@MessageMapping("math.service.square")
	public Mono<ComputationResponseDto> findSquare(Mono<ComputationRequestDto> requestDtoMono)
	{
		return this.service.findSquare(requestDtoMono);
	}
	
	@MessageMapping("math.service.table")
	public Flux<ComputationResponseDto> tableStream(Mono<ComputationRequestDto> requestDtoMono)
	{
		return requestDtoMono.flatMapMany(this.service::tableStream);
	}

	@MessageMapping("math.service.chart")
	public Flux<ChartResponseDto> chartStream(Flux<ComputationRequestDto> requestDtoFlux)
//	public Flux<String> chartStream(Flux<String> requestDtoFlux)
	{
		System.out.println("!!!!!");
		System.out.println("tableStream");
		System.out.println("!!!!!");
		
		return this.service.chartStream(requestDtoFlux);
	}
	
}
