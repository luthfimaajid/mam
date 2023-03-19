package com.etspplbo50.deliveryservice.service;

import com.etspplbo50.deliveryservice.dto.DeliveryRequest;
import com.etspplbo50.deliveryservice.dto.DeliveryResponse;
import com.etspplbo50.deliveryservice.event.OrderDeliveredEvent;
import com.etspplbo50.deliveryservice.event.OrderShippedEvent;
import com.etspplbo50.deliveryservice.model.Delivery;
import com.etspplbo50.deliveryservice.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;

    private final KafkaTemplate<String, OrderShippedEvent> orderShippedEventKafkaTemplate;
    private final KafkaTemplate<String, OrderDeliveredEvent> orderDeliveredEventKafkaTemplate;

    public void orderReadyTopicHandler(String orderId) {
        Random rand = new Random();
        Integer distance = rand.nextInt(20) + 1;

        Delivery delivery = Delivery.builder()
                .orderId(orderId)
                .status("SHIPPED")
                .distance(distance)
                .employeeId("640f4a33d17f4d5cb232fb2b")
                .build();

        deliveryRepository.save(delivery);

        orderShippedEventKafkaTemplate.send("orderShippedTopic", new OrderShippedEvent(orderId, delivery.getEmployeeId(), delivery.getDistance()));

        log.info("Delivering order for {}", orderId);
    }

    public List<DeliveryResponse> getAllDelivery() {
        List<Delivery> deliveryList = deliveryRepository.findAll();

        return deliveryList.stream().map(delivery -> mapDeliveryToKitchenResponse(delivery)).toList();
    }

    public DeliveryResponse getDelivery(String id) {
        Optional<Delivery> delivery = deliveryRepository.findById(id);

        if (delivery.isPresent()) {
            return mapDeliveryToKitchenResponse(delivery.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Delivery not found");
        }
    }

    public DeliveryResponse updateDelivery(String id, DeliveryRequest deliveryRequest) {
        Optional<Delivery> delivery = deliveryRepository.findById(id);

        if (delivery.isPresent()) {
            Delivery newDelivery = delivery.get();

            newDelivery.setStatus("SETTLED");

            deliveryRepository.save(newDelivery);
            return mapDeliveryToKitchenResponse(newDelivery);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Delivery not found");
        }
    }

    public DeliveryResponse updateDeliveryStatus(String id) {
        Optional<Delivery> delivery = deliveryRepository.findById(id);

        if (delivery.isPresent()) {
            Delivery newDelivery = delivery.get();

            newDelivery.setStatus("DELIVERED");

            newDelivery = deliveryRepository.save(newDelivery);

            orderDeliveredEventKafkaTemplate.send(
                    "orderDeliveredTopic",
                    OrderDeliveredEvent.builder()
                            .orderId(newDelivery.getOrderId())
                            .employeeId(newDelivery.getEmployeeId())
                            .distance(newDelivery.getDistance())
                            .startTime(newDelivery.getStartTime())
                            .endTime(newDelivery.getEndTime())
                    .build()
            );

            return mapDeliveryToKitchenResponse(newDelivery);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Delivery not found");
        }
    }

    public String deleteDelivery(String id) {
        Optional<Delivery> delivery = deliveryRepository.findById(id);

        if (delivery.isPresent()) {
            deliveryRepository.deleteById(id);
            return "Delivery deleted";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Delivery not found");
        }
    }

    private DeliveryResponse mapDeliveryToKitchenResponse(Delivery delivery) {
        return DeliveryResponse.builder()
                .id(delivery.getId())
                .orderId(delivery.getOrderId())
                .employeeId(delivery.getEmployeeId())
                .distance(delivery.getDistance())
                .status(delivery.getStatus())
                .startTime(delivery.getStartTime())
                .endTime(delivery.getEndTime())
                .build();
    }
}
