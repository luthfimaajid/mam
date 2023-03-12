package com.etspplbo50.orderservice.repository;


import com.etspplbo50.orderservice.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {

}
