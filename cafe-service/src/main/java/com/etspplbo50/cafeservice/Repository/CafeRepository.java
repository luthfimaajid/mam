package com.etspplbo50.cafeservice.Repository;

import com.etspplbo50.cafeservice.Model.Cafe;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CafeRepository extends MongoRepository<Cafe, String> {

}
