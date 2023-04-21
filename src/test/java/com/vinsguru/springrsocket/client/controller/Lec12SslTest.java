package com.vinsguru.springrsocket.client.controller;

import java.net.URI;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Mono;
import reactor.netty.tcp.TcpClient;
import reactor.test.StepVerifier;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec12SslTest {

	static 
	{
		System.setProperty("javax.net.ssl.trustStore", "/Users/brennan/Downloads/client.truststore");
		System.setProperty("javax.net.ssl.trustPassword", "password");
	}
	
	@Autowired
	private RSocket rsocket;
	
	@BeforeAll
	public void setup()
	{
		this.rsocket = RSocketConnector.create()
				.connect(WebsocketClientTransport.create(URI.create("wss://localhost:6565")))
				.block();
	}
	
	
	@Test
	public void connectionTest()
	{
		Payload payload = DefaultPayload.create("java client");
		Mono<Void> mono = this.rsocket.fireAndForget(payload);
		
		StepVerifier.create(mono).verifyComplete();
	}
}
