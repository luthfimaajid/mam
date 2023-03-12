package com.etspplbo50.orderservice.config;

import com.etspplbo50.orderservice.event.OrderPlacedEvent;
import com.etspplbo50.orderservice.model.Order;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @ConfigurationProperties(prefix = "spring.kafka.template.first.producer")
    public Map<String, Object> firstProducerConfigs() {
        return new HashMap<>();
    }

    @ConfigurationProperties(prefix = "spring.kafka.template.second.producer")
    public Map<String, Object> secondProducerConfigs() {
        return new HashMap<>();
    }

    @Bean(name = "firstKafkaTemplate")
    public KafkaTemplate<String, OrderPlacedEvent> firstKafkaTemplate() {
        ProducerFactory<String, OrderPlacedEvent> producerFactory = new DefaultKafkaProducerFactory<>(firstProducerConfigs());
        KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setDefaultTopic("orderPlaced");
        return kafkaTemplate;
    }

    @Bean(name = "secondKafkaTemplate")
    public KafkaTemplate<String, String> secondKafkaTemplate() {
        ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(secondProducerConfigs());
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setDefaultTopic("my-default-topic");
        return kafkaTemplate;
    }

}
