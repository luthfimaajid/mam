package com.etspplbo50.customerservice.Service;

import com.etspplbo50.customerservice.DTO.CustomerRequest;
import com.etspplbo50.customerservice.DTO.CustomerResponse;
import com.etspplbo50.customerservice.Model.Customer;
import com.etspplbo50.customerservice.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        Customer customer = Customer.builder()
                .name(customerRequest.getName())
                .phoneNumber(customerRequest.getPhoneNumber())
                .address(customerRequest.getAddress())
                .latitude(customerRequest.getLatitude())
                .longitude(customerRequest.getLongitude())
                .build();

        customerRepository.save(customer);

        return mapModeltoResponse(customer);
    }

    public List<CustomerResponse> getAllCustomer() {
        List<Customer> customerList = customerRepository.findAll();

        return customerList.stream().map(customer -> mapModeltoResponse(customer)).toList();
    }

    public CustomerResponse getCustomer(String id) {
        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isPresent()) {
            return mapModeltoResponse(customer.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }
    }

    public CustomerResponse updateCustomer(String id, CustomerRequest customerRequest) {
        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isPresent()) {
            Customer newCustomer = customer.get();

            newCustomer.setName(customerRequest.getName());
            newCustomer.setPhoneNumber(customerRequest.getPhoneNumber());
            newCustomer.setAddress(customerRequest.getAddress());
            newCustomer.setLatitude(customerRequest.getLatitude());
            newCustomer.setLongitude(customerRequest.getLongitude());

            customerRepository.save(newCustomer);
            return mapModeltoResponse(newCustomer);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }
    }

    public String deleteCustomer(String id) {
        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isPresent()) {
            customerRepository.deleteById(id);
            return "Customer deleted";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }
    }

    private CustomerResponse mapModeltoResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .latitude(customer.getLatitude())
                .longitude(customer.getLongitude())
                .build();
    }
}
