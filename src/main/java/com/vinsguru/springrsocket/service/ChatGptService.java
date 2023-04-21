package com.vinsguru.springrsocket.service;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

public class ChatGptService {
	
	@Autowired
	private Environment env;
	
	public String chat(final List<ChatMessage> messages)
	{
		final StringBuffer buffer = new StringBuffer();

		final OpenAiService service = new OpenAiService(env.getProperty("openai.key"), Duration.ofSeconds(60));

		final ChatCompletionRequest completionRequest = ChatCompletionRequest.builder().messages(messages).model("gpt-3.5-turbo").n(1).build();
		final List<ChatCompletionChoice> choices = service.createChatCompletion(completionRequest).getChoices();

		for (final ChatCompletionChoice choice : choices)
		{
			buffer.append(choice.getMessage().getContent());
		}

		return buffer.toString().trim();
	}
	
}
