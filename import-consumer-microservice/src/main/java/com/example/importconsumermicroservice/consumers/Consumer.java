package com.example.importconsumermicroservice.consumers;

import com.example.importconsumermicroservice.pojos.Message;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Collections;

@Service
public class Consumer
{
    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group-id}")
    public void receiveUpdate(Message message) throws IOException {


        System.out.println("Received a message : "+ message.getUrl());
        RestTemplate restTemplate = new RestTemplate();

        String uri="http://localhost:3000/train/"+message.getUrl();
        restTemplate.getForEntity(uri,String.class);
    }
}