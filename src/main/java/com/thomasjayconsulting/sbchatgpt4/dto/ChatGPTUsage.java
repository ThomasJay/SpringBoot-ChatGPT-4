package com.thomasjayconsulting.sbchatgpt4.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ChatGPTUsage {
    @SerializedName(value="prompt_tokens")
    private Integer promptTokens;

    @SerializedName(value="completion_tokens")
    private Integer completionTokens;

    @SerializedName(value="total_tokens")
    private Integer totalTokens;
}
