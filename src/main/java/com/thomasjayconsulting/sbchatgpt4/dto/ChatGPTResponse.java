package com.thomasjayconsulting.sbchatgpt4.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatGPTResponse {
    private String id;
    private String model;
    private List<ChatGPTChoice> choices;
    private ChatGPTUsage usage;
}
