package com.etspplbo50.customerservice.Repository;

import com.etspplbo50.customerservice.Model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {

}
