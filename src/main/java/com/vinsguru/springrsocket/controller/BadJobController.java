package com.vinsguru.springrsocket.controller;

import java.time.Duration;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;

import reactor.core.publisher.Mono;

@Controller
public class BadJobController {

	@MessageMapping("batch.job.request")
	public Mono<Void> submitJob(Mono<Integer> integerMono, RSocketRequester rSocketRequester)
	{
		this.process(integerMono, rSocketRequester);
		return Mono.empty();
	}
	
	private void process(Mono<Integer> integerMono, RSocketRequester rSocketRequester)
	{
		integerMono
					.delayElement(Duration.ofSeconds(10))
					.map(i -> i * i * i)
					.flatMap(i -> rSocketRequester.route("batch.job.response").data(i).send())
					.subscribe();
	}
}
