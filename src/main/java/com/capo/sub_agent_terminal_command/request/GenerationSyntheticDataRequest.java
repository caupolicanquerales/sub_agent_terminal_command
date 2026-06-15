package com.capo.sub_agent_terminal_command.request;

import java.util.List;

public class GenerationSyntheticDataRequest {
	
	private String prompt;
	private List<ToolIformation> toolRequest;
	private List<String> imageReferences;

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public List<ToolIformation> getToolRequest() {
		return toolRequest;
	}

	public void setToolRequest(List<ToolIformation> toolRequest) {
		this.toolRequest = toolRequest;
	}

	public List<String> getImageReferences() {
		return imageReferences;
	}

	public void setImageReferences(List<String> imageReferences) {
		this.imageReferences = imageReferences;
	}
	
	
}
