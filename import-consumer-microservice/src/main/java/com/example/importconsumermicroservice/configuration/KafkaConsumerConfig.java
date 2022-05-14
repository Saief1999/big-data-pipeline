package com.example.importconsumermicroservice.configuration;

import com.example.importconsumermicroservice.pojos.Message;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerPartitionAssignor;
import org.apache.kafka.clients.consumer.RoundRobinAssignor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${kafka.host}")
    private String kafkaHost;

    @Value("${kafka.group-id}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, Message> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                kafkaHost);
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                groupId);
        props.put(
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest"
        );
        props.put(
                ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,
                RoundRobinAssignor.class.getName()
        ); // setting the partition strategy to Round Robin, so each service consumes a message at a time

        JsonDeserializer<Message> jsonDeserializer = new JsonDeserializer<>();

        Map<String, Object> idMappings = new HashMap<>();
        // we map UpdateMessage -> Our Class Instance
        idMappings.put(JsonDeserializer.TYPE_MAPPINGS,
                "Message:"+Message.class.getName());

        idMappings.put(
                JsonDeserializer.TRUSTED_PACKAGES,
                "*"
        );

        //mind this `false` -- they have different modes for key and value deserializers
        jsonDeserializer.configure(idMappings, false);

        return new DefaultKafkaConsumerFactory<String, Message>(props, new StringDeserializer(),jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Message>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, Message> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}