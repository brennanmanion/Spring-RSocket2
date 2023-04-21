package com.vinsguru.springrsocket.controller;

import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;

import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.vinsguru.springrsocket.service.ChatGptService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class ChatController {
	

	@Autowired
	private ChatGptService chatGptService;
	
    private final Sinks.Many<byte[]> commonMessageSink = Sinks.many().multicast().directBestEffort();
    private List<String> users = new ArrayList<>();
    private final Sinks.Many<byte[]> isRenderResponses = Sinks.many().multicast().directBestEffort();
    private List<String> prompts = new ArrayList<>();

    @ConnectMapping
    public void connect(@Headers Map<String, Object> headers)
    {
    	System.out.println("connected!");
        commonMessageSink.tryEmitNext("someone connected".getBytes());
    }

    @MessageMapping("chatSend")
    public void chatSend(byte[] messagePayload) {
    	System.out.println(new String(messagePayload));
//    	commonMessageSink.tryEmitNext(("someone said: " + new String(messagePayload)).getBytes());
    	commonMessageSink.tryEmitNext(new String(messagePayload).getBytes());
    }
    
    @MessageMapping("setUser")
    public void setUser(byte[] messagePayload) {
    	final String user = new String(messagePayload);
    	users.add(user);
    	System.out.println();
    }
    
    @MessageMapping("chatReceive")
    public Flux<byte[]> chatReceive()
    {
    	return commonMessageSink.asFlux();
    }
    
    @MessageMapping("isRenderResponses")
    public Flux<byte[]> isRenderResponses()
    {
    	return isRenderResponses.asFlux();
    }
    
    @MessageMapping("startRound")
    public void startRound()
    {
    	prompts.add("One");
    	prompts.add("Two");
    	prompts.add("Three");
    	
    	System.out.println(users);
    	if (!users.isEmpty())
    	{
        	Random random = new Random();
        	int randomIndex = random.nextInt(users.size());
        	String userAction = users.remove(randomIndex);
        	isRenderResponses.tryEmitNext(userAction.getBytes());
        	commonMessageSink.tryEmitNext(prompts.remove(0).getBytes());
        	
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
              int count = 30;
              
              @Override
              public void run() {
                
                commonMessageSink.tryEmitNext(("Task executed " + count + " times.").getBytes());
                if (count == 0) {
                  timer.cancel();
                }
                count--;
              }
            };
            timer.schedule(task, 0, 1000);
    	}
    	else
    	{
    		commonMessageSink.tryEmitNext(("Game over!").getBytes());
    	}
    }
    
    @MessageMapping("sendTilt")
    public void sendTilt()
    {
    	if (!prompts.isEmpty())
    	{
        	Random random = new Random();
        	int randomIndex = random.nextInt(prompts.size());
        	String prompt = prompts.remove(randomIndex);
        	isRenderResponses.tryEmitNext(prompt.getBytes());
    	}
    }
    
    public void setMessages(final String user, final List<String> submissions)
    {
    	final List<ChatMessage> messages = new ArrayList<ChatMessage>();
		
		{
			final ChatMessage message = new ChatMessage();
			message.setRole(ChatMessageRole.SYSTEM.value());
			message.setContent("You are a creative, coming up with charades themes for a party game. Use the context provided to come up with relevant themes.");
			
			messages.add(message);
		}
		
		{
			final ChatMessage message = new ChatMessage();
			message.setRole(ChatMessageRole.ASSISTANT.value());
			message.setContent("Context one is " + "stocks");

			messages.add(message);
		}

//    	{
//			final ChatMessage message = new ChatMessage();
//			message.setContent("");
//			message.setRole(ChatMessageRole.USER.value());
//
//			messages.add(message);
//		}
		

		
		final String result = chatGptService.chat(messages);
    }
}
