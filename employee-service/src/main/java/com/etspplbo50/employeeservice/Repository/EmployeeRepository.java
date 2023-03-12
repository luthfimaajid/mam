package com.etspplbo50.employeeservice.Repository;

import com.etspplbo50.employeeservice.Model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployeeRepository extends MongoRepository<Employee, String> {

}
