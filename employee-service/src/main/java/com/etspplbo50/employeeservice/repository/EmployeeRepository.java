package com.etspplbo50.employeeservice.repository;

import com.etspplbo50.employeeservice.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployeeRepository extends MongoRepository<Employee, String> {

}
