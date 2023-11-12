package com.thomasjayconsulting.sbchatgpt4.service;

import com.google.gson.Gson;
import com.thomasjayconsulting.sbchatgpt4.dto.ChatGPTMessage;
import com.thomasjayconsulting.sbchatgpt4.dto.ChatGPTRequest;
import com.thomasjayconsulting.sbchatgpt4.dto.ChatGPTResponse;
import com.thomasjayconsulting.sbchatgpt4.dto.ChatGPTUsage;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ChatGPTService {

    @Value("${chatgpt.url}")
    private String CHATGPT_URL;

    @Value("${chatgpt.key}")
    private String CHATGPT_KEY;

    @Value("${chatgpt.model}")
    private String GPT_MODEL;

    @Value("#{new Double('${chatgpt.default.temperature}')}")
    private Double DEFAULT_TEMPERATURE;

    @Value("#{new Integer('${chatgpt.default.maxtokens}')}")
    private Integer DEFAULT_MAX_TOKENS;


    private Map<Integer, List<ChatGPTMessage>> seedMessagesList = new HashMap<Integer, List<ChatGPTMessage>>();

    public Map<String, Object> system(String message, int seed) {

        // Try to get all the messages for this seed!!!
        List<ChatGPTMessage> seedMessages = seedMessagesList.get(seed);

        // If no messages for thsi seed, create empty holder
        if (seedMessages == null) {
            seedMessages = new ArrayList<ChatGPTMessage>();
        }

        // Add in this system message
        seedMessages.add(new ChatGPTMessage("system", message));

        seedMessagesList.put(seed, seedMessages);

        log.info("system updated seed List: {}", seedMessagesList.get(seed).size());

        Map<String, Object> map = new HashMap<>();

        map.put("status", "Success");
        map.put("content", "System Message Saved");

        return map;

    }

    public Map<String, Object> search(String query, int seed) {

        log.info("search started. query: {} seed: {}", query, seed);

        ChatGPTRequest chatGPTRequest = new ChatGPTRequest();
        chatGPTRequest.setModel(GPT_MODEL);
        chatGPTRequest.setTemperature(DEFAULT_TEMPERATURE);
        chatGPTRequest.setMaxTokens(DEFAULT_MAX_TOKENS);

        chatGPTRequest.setSeed(seed);

        // Try to get all the messages for this seed!!!
        List<ChatGPTMessage> seedMessages = seedMessagesList.get(seed);

        // If no messages for thsi seed, create empty holder
        if (seedMessages == null) {
            seedMessages = new ArrayList<ChatGPTMessage>();
        }

        // Add in this user message
        seedMessages.add(new ChatGPTMessage("user", query));

        seedMessagesList.put(seed, seedMessages);

        log.info("user updated seed List: {}", seedMessagesList.get(seed).size());


        // Add to payload
        chatGPTRequest.setMessages(seedMessages);



        log.info("search started. url: {}", CHATGPT_URL);

        HttpPost post = new HttpPost(CHATGPT_URL);
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer " + CHATGPT_KEY);

        Gson gson = new Gson();

        String body = gson.toJson(chatGPTRequest);

        log.info("search post body: {}", body);

        try {
            final StringEntity entity = new StringEntity(body);
            post.setEntity(entity);

            try (CloseableHttpClient httpClient = HttpClients.custom().build();
                 CloseableHttpResponse response = httpClient.execute(post)) {

                String responseBody = EntityUtils.toString(response.getEntity());


                log.info("search got a status: {}", response.getStatusLine());
                log.info("search got a responseBody: {}", responseBody);

                ChatGPTResponse chatGPTResponse = gson.fromJson(responseBody, ChatGPTResponse.class);

                ChatGPTUsage usage = chatGPTResponse.getUsage();

                log.info("search got a response: {}", chatGPTResponse);
                log.info("search got a usage: {}", usage);


                String responseContent =  chatGPTResponse.getChoices().get(0).getMessage().getContent();

                Map<String, Object> map = new HashMap<>();

                map.put("status", "Success");
                map.put("content", responseContent);

                return map;

            } catch (Exception e) {
                log.info("search exception A e: {}", e.getMessage());

                Map<String, Object> map = new HashMap<>();

                map.put("status", "Failed");
                map.put("content", e.getMessage());

                return map;
            }
        }
        catch (Exception e) {
            log.info("search exception B e: {}", e.getMessage());

            Map<String, Object> map = new HashMap<>();

            map.put("status", "Failed");
            map.put("content", e.getMessage());

            return map;
        }

    }
}
