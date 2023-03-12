package com.etspplbo50.customerservice.repository;

import com.etspplbo50.customerservice.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {

}
