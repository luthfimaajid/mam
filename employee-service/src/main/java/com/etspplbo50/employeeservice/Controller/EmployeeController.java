package com.etspplbo50.employeeservice.Controller;

import com.etspplbo50.employeeservice.DTO.EmployeeRequest;
import com.etspplbo50.employeeservice.DTO.EmployeeResponse;
import com.etspplbo50.employeeservice.Service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse createEmployee(@RequestBody EmployeeRequest employeeRequest) {
        return employeeService.createEmployee(employeeRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeResponse getEmployee(@PathVariable("id") String id) {
        return employeeService.getEmployee(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeResponse> getAllEmployee() {
        return employeeService.getAllEmployee();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeResponse updateEmployee(@PathVariable("id") String id, @RequestBody EmployeeRequest employeeRequest) {
        return employeeService.updateEmployee(id, employeeRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteEmployee(@PathVariable("id") String id) {
        return employeeService.deleteEmployee(id);
    }
}
