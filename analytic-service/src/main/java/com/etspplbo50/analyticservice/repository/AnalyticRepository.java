package com.etspplbo50.analyticservice.repository;


import com.etspplbo50.analyticservice.model.Analytic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AnalyticRepository extends MongoRepository<Analytic, String> {
    Optional<Analytic> findByOrderId(String orderId);
}
