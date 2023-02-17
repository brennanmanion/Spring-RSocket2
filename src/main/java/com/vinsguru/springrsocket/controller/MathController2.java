package com.vinsguru.springrsocket.controller;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;

import com.vinsguru.springrsocket.dto.ChartResponseDto;
import com.vinsguru.springrsocket.dto.ComputationRequestDto;
import com.vinsguru.springrsocket.dto.ComputationResponseDto;
import com.vinsguru.springrsocket.service.MathService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class MathController2 {

	@Autowired
	private MathService service;
	
//    @ConnectMapping
//    public void connect(@Headers Map<String, Object> headers) {
//        System.out.println("connect");
//        headers.forEach((key, value) -> System.out.println("connect header {" + key + "} = {" + value + "}"));
//    }

    @MessageMapping("hello")
    public Flux<byte[]> responseStream(Flux<byte[]> rsMessageStream) {
        System.out.println("'hello' route called");
        
        return rsMessageStream
                .log()
                .flatMap(rsMessage -> Flux.range(1, 2)
                        .map(cnt -> {
                            byte[] resp = new byte[rsMessage.length];
                            IntStream.range(0, rsMessage.length)
                                    .forEach(i -> {
                                        resp[rsMessage.length - i - 1] = (byte) (rsMessage[i] + cnt);
                                    });
                            return resp;
                        }))
                .delayElements(Duration.ofMillis(500));
    }
}
