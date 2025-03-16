package com.afci.service;

import com.afci.data.Customer;
import com.afci.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Customer customer) {
        if (customerRepository.existsById(customer.getId())) {
            return customerRepository.save(customer);
        }
        throw new RuntimeException("Client non trouvé avec l'ID : " + customer.getId());
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    public List<Customer> findByEmail(String email) {
        return customerRepository.findByEmailContainingIgnoreCase(email);
    }
}