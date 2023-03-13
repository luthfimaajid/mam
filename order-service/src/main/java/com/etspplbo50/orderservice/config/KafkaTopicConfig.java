package com.etspplbo50.orderservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic orderPlacedTopic() {
        return TopicBuilder.name("orderPlacedTopic")
                .build();
    }

    @Bean
    public NewTopic paymentSettledTopic() {
        return TopicBuilder.name("paymentSettledTopic")
                .build();
    }
    @Bean
    public NewTopic orderReadyTopic() {
        return TopicBuilder.name("orderReadyTopic")
                .build();
    }

    @Bean
    public NewTopic orderDeliveredTopic() {
        return TopicBuilder.name("orderDeliveredTopic")
                .build();
    }
}
