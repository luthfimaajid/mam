package com.etspplbo50.orderservice.service;


import com.etspplbo50.orderservice.dto.OrderLineItemsRequest;
import com.etspplbo50.orderservice.dto.OrderRequest;
import com.etspplbo50.orderservice.dto.OrderResponse;
import com.etspplbo50.orderservice.event.OrderPlacedEvent;
import com.etspplbo50.orderservice.model.Order;
import com.etspplbo50.orderservice.model.OrderLineItems;
import com.etspplbo50.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;

    private final WebClient.Builder webClient;

    private final KafkaTemplate<String, OrderPlacedEvent> orderPlacedKafkaTemplate;

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

            orderPlacedKafkaTemplate.send("orderPlacedTopic", new OrderPlacedEvent(order.getId()));

            return "Order placed successfully please pay the order";

//        } else {
//            throw new IllegalArgumentException("Product is not in stock");
//        }
    }

    public List<OrderResponse> getAllOrder() {
        List<Order> orderList = orderRepository.findAll();

        return orderList.stream().map(order -> mapOrderToOrderResponse(order)).toList();
    }

    public OrderResponse getOrder(String id) {
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            return mapOrderToOrderResponse(order.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
    }
    public void paymentSettledTopicHandler(String id) {
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            Order newOrder = order.get();

            newOrder.setStatus("PREPARING");

            log.info("Order payment settled, proceding to prepare");
            orderRepository.save(newOrder);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
    }

    public void orderReadyToDeliverTopicHandler(String id) {
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            Order newOrder = order.get();

            newOrder.setStatus("WAITING FOR DELIVERY");

            log.info("Waiting for employee to deliver order");
            orderRepository.save(newOrder);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
    }



    private OrderLineItems parseOrderLineItemsRequestToModel(OrderLineItemsRequest orderLineItemsRequest) {
        return OrderLineItems.builder()
                .menuId(orderLineItemsRequest.getMenuId())
                .price(orderLineItemsRequest.getPrice())
                .quantity(orderLineItemsRequest.getQuantity())
                .build();
    }

    private OrderResponse mapOrderToOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .cafeId(order.getCafeId())
                .customerId(order.getCustomerId())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .note(order.getNote())
                .orderLineItemsList(order.getOrderLineItemsList())
                .build();
    }
}
