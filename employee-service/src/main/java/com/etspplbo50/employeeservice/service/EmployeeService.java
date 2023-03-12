package com.etspplbo50.employeeservice.service;

import com.etspplbo50.employeeservice.dto.EmployeeRequest;
import com.etspplbo50.employeeservice.dto.EmployeeResponse;
import com.etspplbo50.employeeservice.model.Employee;
import com.etspplbo50.employeeservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
        Employee employee = Employee.builder()
                .cafeId(employeeRequest.getCafeId())
                .name(employeeRequest.getName())
                .isAbleDelivery(false)
                .build();

        employeeRepository.save(employee);

        return mapModelToResponse(employee);
    }

    public List<EmployeeResponse> getAllEmployee() {
        List<Employee> employeeList = employeeRepository.findAll();

        return employeeList.stream().map(employee -> mapModelToResponse(employee)).toList();
    }

    public EmployeeResponse getEmployee(String id) {
        Optional<Employee> employee = employeeRepository.findById(id);

        if (employee.isPresent()) {
            return mapModelToResponse(employee.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }
    }

    public EmployeeResponse updateEmployee(String id, EmployeeRequest employeeRequest) {
        Optional<Employee> employee = employeeRepository.findById(id);

        if (employee.isPresent()) {
            Employee newEmployee = employee.get();

            newEmployee.setName(employeeRequest.getName());
            newEmployee.setCafeId(employeeRequest.getCafeId());
            newEmployee.setIsAbleDelivery(employeeRequest.getIsAbleDelivery());

            employeeRepository.save(newEmployee);
            return mapModelToResponse(newEmployee);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }
    }

    public String deleteEmployee(String id) {
        Optional<Employee> employee = employeeRepository.findById(id);

        if (employee.isPresent()) {
            employeeRepository.deleteById(id);
            return "Employee deleted";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }
    }

    private EmployeeResponse mapModelToResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .cafeId(employee.getCafeId())
                .name(employee.getName())
                .isAbleDelivery(employee.getIsAbleDelivery())
                .build();
    }
}
