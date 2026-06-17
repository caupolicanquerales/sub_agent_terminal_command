package com.capo.sub_agent_terminal_command.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.capo.sub_agent_terminal_command.response.DataMessage;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class ExecutingTerminalCommandService {
	
	private final ChatClient chatClient;
	private final String systemPrompt;
	private final RedisTemplate<String, Object> redisTemplate;
	private final static String SHOW_VS_CODE="terminal";
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	public ExecutingTerminalCommandService(@Qualifier("chatClientGeneral") ChatClient chatClient,
			@Qualifier("systemPrompt") String systemPrompt,
			RedisTemplate<String, Object> redisTemplate) {
		this.chatClient = chatClient;
		this.systemPrompt = systemPrompt;
		this.redisTemplate = redisTemplate;
	}
	
	public CompletableFuture<String> generateTerminalCommandAsync(String prompt, List<String> imageReferences){
		String layoutKey = (imageReferences != null && !imageReferences.isEmpty())
				? imageReferences.get(0) : null;
		return CompletableFuture.supplyAsync(() -> {
			String userMessage = "[INPUT_USER_PROMPT: RAW_PROMPT] " + prompt;
			if (layoutKey != null) {
				Object stored = redisTemplate.opsForValue().get(layoutKey);
				if (stored != null) {
					userMessage = userMessage + "\n[INPUT_FORMAT: RAW_INFORMATION] " + stored.toString();
				}
			}
			String content= this.chatClient.prompt()
					.messages(new SystemMessage(systemPrompt))
					.user(userMessage)
					.call()
					.content();
			try {
				DataMessage dataMessage = new DataMessage();
				dataMessage.setType(SHOW_VS_CODE);
				dataMessage.setMessage(content);
				return objectMapper.writeValueAsString(dataMessage);
			} catch (Exception e) {
				return content;
			}
		});
	} 
}
