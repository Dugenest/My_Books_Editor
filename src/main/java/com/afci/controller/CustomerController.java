package com.afci.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afci.data.Customer;
import com.afci.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer Management", description = "Customer operations")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Get all customers")
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @Operation(summary = "Get customer by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Customer>> getCustomerById(
        @Parameter(description = "Customer ID") @PathVariable Long id
    ) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @Operation(summary = "Create customer")
    @PostMapping
    public ResponseEntity<Customer> createCustomer(
        @Parameter(description = "Customer details") 
        @Valid @RequestBody Customer customer
    ) {
        return new ResponseEntity<>(customerService.createCustomer(customer), 
            HttpStatus.CREATED);
    }

    @Operation(summary = "Update customer")
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
        @Parameter(description = "Customer ID") @PathVariable Long id,
        @Parameter(description = "Customer details") 
        @Valid @RequestBody Customer customer
    ) {
        return ResponseEntity.ok(customerService.updateCustomer(customer));
    }

    @Operation(summary = "Delete customer")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(
        @Parameter(description = "Customer ID") @PathVariable Long id
    ) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}