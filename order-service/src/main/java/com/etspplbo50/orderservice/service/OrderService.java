package com.etspplbo50.orderservice.service;


import com.etspplbo50.orderservice.dto.OrderLineItemsRequest;
import com.etspplbo50.orderservice.dto.OrderRequest;
import com.etspplbo50.orderservice.event.OrderPlacedEvent;
import com.etspplbo50.orderservice.model.Order;
import com.etspplbo50.orderservice.model.OrderLineItems;
import com.etspplbo50.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;

    private final WebClient.Builder webClient;

    @Qualifier("firstKafkaTemplate")
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaOrderPlaced;

    public String placeOrder(OrderRequest orderRequest) {
        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsList()
                .stream()
                .map(orderLineItemsRequest -> parseOrderLineItemsRequestToModel(orderLineItemsRequest))
                .toList();

        Integer totalPrice = orderLineItemsList.stream()
                .map(orderLineItem -> orderLineItem.getPrice() * orderLineItem.getQuantity())
                .reduce(0, (a,b) -> a+b);

        Order order = Order.builder()
                .cafeId(orderRequest.getCafeId())
                .customerId(orderRequest.getCustomerId())
                .totalPrice(totalPrice)
                .note(orderRequest.getNote())
                .status("WAITING FOR PAYMENT")
                .orderLineItemsList(orderLineItemsList)
                .build();


//        List<String> menuId = order.getOrderLineItemsList()
//                .stream()
//                .map(orderLineItems -> orderLineItems.getMenuId())
//                .toList();

        // call inventory service and place order if product is instock
//        log.info("sending request to inventory");
//        InventoryResponse[] inventoryResponses = webClient.build().get()
//                .uri("http://inventory-service/api/inventory",
//                        uriBuilder -> uriBuilder
//                                .queryParam("skuCode", skuCodes)
//                                .queryParam("cafeId", )
//                                .build())
//                .retrieve()
//                .bodyToMono(InventoryResponse[].class)
//                .block();
//
//        boolean allProductInStock = Arrays.stream(inventoryResponses).
//                allMatch(inventoryResponse -> inventoryResponse.isInStock());
//
//        if (allProductInStock) {

            Order createdOrder = orderRepository.save(order);

//            createPayment(createdOrder.getOrderNumber(), orderLineItemsList);
//
//            List<OrderLineItemsUpdate> orderLineItemsUpdates = orderLineItemsList.stream().map(orderLineItems -> {
//                return OrderLineItemsUpdate.builder()
//                        .quantity(orderLineItems.getQuantity())
//                        .skuCode(orderLineItems.getSkuCode())
//                        .build();
//            }).toList();

//            log.info("sending 2nd request to inventory");
//            webClient.build().patch()
//                    .uri("http://inventory-service/api/inventory/update-stock")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body(BodyInserters.fromValue(orderLineItemsUpdates))
//                    .retrieve()
//                    .bodyToMono(void.class)
//                    .block();

            kafkaOrderPlaced.send("orderPlaced", new OrderPlacedEvent(order.getId()));

            return "Order placed successfully please pay the order";

//        } else {
//            throw new IllegalArgumentException("Product is not in stock");
//        }
    }

    private OrderLineItems parseOrderLineItemsRequestToModel(OrderLineItemsRequest orderLineItemsRequest) {
        return OrderLineItems.builder()
                .menuId(orderLineItemsRequest.getMenuId())
                .price(orderLineItemsRequest.getPrice())
                .quantity(orderLineItemsRequest.getQuantity())
                .build();
    }
}
