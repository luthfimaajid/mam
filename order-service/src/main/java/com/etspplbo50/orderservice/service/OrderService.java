package com.etspplbo50.orderservice.service;


import com.etspplbo50.orderservice.dto.OrderLineItemsRequest;
import com.etspplbo50.orderservice.dto.OrderRequest;
import com.etspplbo50.orderservice.dto.OrderResponse;
import com.etspplbo50.orderservice.event.OrderPlacedEvent;
import com.etspplbo50.orderservice.model.Order;
import com.etspplbo50.orderservice.model.OrderLineItems;
import com.etspplbo50.orderservice.repository.OrderRepository;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.ServiceInstance;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.etspplbo50.inventoryservicegrpc.*;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final EurekaClient eurekaClient;
    @GrpcClient("inventory")
    private InventoryServiceGrpc.InventoryServiceBlockingStub inventoryServiceBlockingStub;
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

//        InstanceInfo inventoryInstance = eurekaClient.getNextServerFromEureka("inventory-service", false);
//        final ManagedChannel channel = ManagedChannelBuilder.forAddress(inventoryInstance.getHostName(), Integer.parseInt(inventoryInstance.getMetadata().get("gRPC_port")))
//                .usePlaintext()
//                .build();
//
//        InventoryServiceGrpc.InventoryServiceBlockingStub stub = InventoryServiceGrpc.newBlockingStub(channel);

        List<String> menuIdList = order.getOrderLineItemsList()
                .stream()
                .map(orderLineItems -> orderLineItems.getMenuId())
                .toList();

        CheckStockRequest checkStockRequest = CheckStockRequest.newBuilder()
                .setCafeId(order.getCafeId())
                .addAllMenuId(menuIdList)
                .build();

        CheckStockResponse checkStockResponse = inventoryServiceBlockingStub.checkStock(checkStockRequest);

//       channel.shutdownNow();

        if (checkStockResponse.getIsAvailable()) {
            Order createdOrder = orderRepository.save(order);
            orderPlacedKafkaTemplate.send("orderPlacedTopic", new OrderPlacedEvent(createdOrder.getId()));
            return "Order placed successfully please pay the order";
        } else {
            return "Product is not in stock";
        }
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

            log.info("Waiting for available employee to deliver order");
            orderRepository.save(newOrder);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
    }

    public void orderShippedTopicHandler(String id) {
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            Order newOrder = order.get();

            newOrder.setStatus("DELIVERING");

            log.info("Order {} is being shipped to the customer", id);
            orderRepository.save(newOrder);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
    }

    public void orderDeliveredTopicHandler(String id) {
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            Order newOrder = order.get();

            newOrder.setStatus("COMPLETED");

            log.info("Order is delivered to the customer, order {} is completed", id);
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
