package com.etspplbo50.cafeservice.repository;

import com.etspplbo50.cafeservice.model.Cafe;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CafeRepository extends MongoRepository<Cafe, String> {

}
