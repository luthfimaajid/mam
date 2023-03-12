package com.etspplbo50.orderservice.service;


import com.etspplbo50.orderservice.DTO.OrderLineItemsRequest;
import com.etspplbo50.orderservice.DTO.OrderRequest;
import com.etspplbo50.orderservice.model.Order;
import com.etspplbo50.orderservice.model.OrderLineItems;
import com.etspplbo50.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;

    private final WebClient.Builder webClient;

    public String placeOrder(OrderRequest orderRequest) {
        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsRequestList()
                .stream()
                .map(orderLineItemsRequest -> parseOrderLineItemsRequestToModel(orderLineItemsRequest))
                .toList();

        Order order = Order.builder()
                .cafeId(orderRequest.getCafeId())
                .customerId(orderRequest.getCustomerId())
                .note(orderRequest.getNote())
                .orderLineItemsList(orderLineItemsList)
                .build();

        List<String> menuId = order.getOrderLineItemsList()
                .stream()
                .map(orderLineItems -> orderLineItems.getMenuId())
                .toList();

        // call inventory service and place order if product is instock
        log.info("sending request to inventory");
        InventoryResponse[] inventoryResponses = webClient.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder
                                .queryParam("skuCode", skuCodes)
                                .queryParam("cafeId", )
                                .build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductInStock = Arrays.stream(inventoryResponses).
                allMatch(inventoryResponse -> inventoryResponse.isInStock());

        if (allProductInStock) {
            Order createdOrder = orderRepository.save(order);
            createPayment(createdOrder.getOrderNumber(), orderLineItemsList);

            List<OrderLineItemsUpdate> orderLineItemsUpdates = orderLineItemsList.stream().map(orderLineItems -> {
                return OrderLineItemsUpdate.builder()
                        .quantity(orderLineItems.getQuantity())
                        .skuCode(orderLineItems.getSkuCode())
                        .build();
            }).toList();


            log.info("sending 2nd request to inventory");
            webClient.build().patch()
                    .uri("http://inventory-service/api/inventory/update-stock")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(orderLineItemsUpdates))
                    .retrieve()
                    .bodyToMono(void.class)
                    .block();

            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));

            return "Order placed successfully";

        } else {
            throw new IllegalArgumentException("Product is not in stock");
        }
    }

    public void createPayment(String orderNumber, List<OrderLineItems> orderLineItems) {
        BigDecimal total_price = orderLineItems.stream()
                .map(orderLineItem -> orderLineItem.getPrice().multiply(BigDecimal.valueOf(orderLineItem.getQuantity())))
                .reduce(BigDecimal.valueOf(0), (a, b) -> a.add(b));


        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderNumber(orderNumber)
                .totalPrice(total_price)
                .build();

        webClient.build().post()
                .uri("http://payment-service/api/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(paymentRequest), PaymentRequest.class)
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .block();
    }

    private OrderLineItems parseOrderLineItemsRequestToModel(OrderLineItemsRequest orderLineItemsRequest) {
        return OrderLineItems.builder()
                .menuId(orderLineItemsRequest.getMenuId())
                .quantity(orderLineItemsRequest.getQuantity())
                .build();
    }
}
