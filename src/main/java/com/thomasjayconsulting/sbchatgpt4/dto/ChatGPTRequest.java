package com.thomasjayconsulting.sbchatgpt4.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class ChatGPTRequest {

    private String model;
    private List<ChatGPTMessage> messages;
    private Double temperature;

    @SerializedName(value="max_tokens")
    private Integer maxTokens;

    private Integer seed;

}
