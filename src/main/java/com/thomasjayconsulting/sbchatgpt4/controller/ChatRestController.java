package com.thomasjayconsulting.sbchatgpt4.controller;

import com.thomasjayconsulting.sbchatgpt4.dto.SearchRequest;
import com.thomasjayconsulting.sbchatgpt4.dto.SystemRequest;
import com.thomasjayconsulting.sbchatgpt4.service.ChatGPTService;
import jdk.jfr.Percentage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class ChatRestController {

    @Autowired
    ChatGPTService chatGPTService;

    @PostMapping("/system/{seed}")
    public ResponseEntity<?> system(@RequestBody SystemRequest systemRequest, @PathVariable int seed) {
        Map<String, Object> response =  chatGPTService.system(systemRequest.getMessage(), seed);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/search/{seed}")
    public ResponseEntity<?> search(@RequestBody SearchRequest searchRequest, @PathVariable int seed) {
        Map<String, Object> response =  chatGPTService.search(searchRequest.getQuery(), seed);

        return new ResponseEntity(response, HttpStatus.OK);
    }




}
