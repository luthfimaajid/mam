package com.etspplbo50.orderservice.controller;

import com.programmingtechie.orderservice.dto.OrderRequest;
import com.programmingtechie.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
//    @CircuitBreaker(name="inventory", fallbackMethod = "fallbackMethod")
//    @TimeLimiter(name="inventory")
//    @Retry(name="inventory")
//    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
//        return CompletableFuture.supplyAsync(() -> orderService.placeOrder(orderRequest));
//    }

    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.placeOrder(orderRequest);
    }

//    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
//        return CompletableFuture.supplyAsync(() -> "Oops! something went wrong, please try to order after some time");
//
//    }
}
