package com.etspplbo50.deliveryservice.repository;

import com.etspplbo50.deliveryservice.model.Delivery;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeliveryRepository extends MongoRepository<Delivery, String> {

}
