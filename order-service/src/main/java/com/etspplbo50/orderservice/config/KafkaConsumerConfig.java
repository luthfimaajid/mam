package com.etspplbo50.orderservice.config;

import com.etspplbo50.orderservice.event.OrderDeliveredEvent;
import com.etspplbo50.orderservice.event.OrderReadyEvent;
import com.etspplbo50.orderservice.event.OrderShippedEvent;
import com.etspplbo50.orderservice.event.PaymentSettledEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    public Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }

    @Bean
    public ConsumerFactory<String, PaymentSettledEvent> paymentSettledEventConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(), new JsonDeserializer<>(PaymentSettledEvent.class, false));
    }
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, PaymentSettledEvent>>
    paymentSettledEventListenerFactory(
            ConsumerFactory<String, PaymentSettledEvent> paymentSettledEventConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, PaymentSettledEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentSettledEventConsumerFactory);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, OrderReadyEvent> orderReadyEventConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(), new JsonDeserializer<>(OrderReadyEvent.class, false));
    }
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, OrderReadyEvent>>
    orderReadyEventListenerFactory(
            ConsumerFactory<String, OrderReadyEvent> orderReadyEventConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, OrderReadyEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderReadyEventConsumerFactory);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, OrderShippedEvent> orderShippedEventConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(), new JsonDeserializer<>(OrderShippedEvent.class, false));
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, OrderShippedEvent>>
    orderShippedEventListenerFactory(
            ConsumerFactory<String, OrderShippedEvent> orderShippedEventConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, OrderShippedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderShippedEventConsumerFactory);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, OrderDeliveredEvent> orderDeliveredEventConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(), new JsonDeserializer<>(OrderDeliveredEvent.class, false));
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, OrderDeliveredEvent>>
    orderDeliveredEventListenerFactory(
            ConsumerFactory<String, OrderDeliveredEvent> orderDeliveredEventConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, OrderDeliveredEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderDeliveredEventConsumerFactory);
        return factory;
    }
}
