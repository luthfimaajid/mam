package com.etspplbo50.paymentservice;

import com.etspplbo50.paymentservice.event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class PaymentServiceApplicaton {
    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplicaton.class, args);
    }


}
