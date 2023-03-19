package com.etspplbo50.employeeservice.repository;

import com.etspplbo50.employeeservice.model.Employee;
import com.google.common.base.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployeeRepository extends MongoRepository<Employee, String> {
    Optional<Employee> findByCafeIdAndIsAbleDelivery(String cafeId, Boolean isAbleDelivery);
}
