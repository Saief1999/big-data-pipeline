package com.example.importmicroservice.producers;

import com.example.importmicroservice.pojos.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class Producer {

    @Value("${kafka.topic}")
    private String projectorTopic;

    private KafkaTemplate<String, Message> kafkaTemplate;

    public Producer(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUpdate(Message Message) {
        System.out.println(String.format("#### -> Producing message -> %s", Message));
        this.kafkaTemplate.send(projectorTopic, Message);
    }
}