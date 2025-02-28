package com.afci.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.afci.data.Customer;
import com.afci.data.CustomerRepository;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setEmail("test@example.com");
        customer.setFirstName("John");
        customer.setLastName("Doe");
    }

    @Test
    void getAllCustomers_ShouldReturnAllCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> result = customerService.getAllCustomers();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(customerRepository).findAll();
    }

    @Test
    void getCustomerById_ShouldReturnCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.getCustomerById(1L);

        assertTrue(result.isPresent());
        assertEquals(customer.getEmail(), result.get().getEmail());
    }

    @Test
    void createCustomer_ShouldReturnSavedCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer result = customerService.createCustomer(customer);

        assertNotNull(result);
        assertEquals(customer.getEmail(), result.getEmail());
    }

    @Test
    void updateCustomer_WhenExists_ShouldReturnUpdatedCustomer() {
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer result = customerService.updateCustomer(customer);

        assertNotNull(result);
        assertEquals(customer.getEmail(), result.getEmail());
    }

    @Test
    void updateCustomer_WhenNotExists_ShouldThrowException() {
        when(customerRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> customerService.updateCustomer(customer));
    }

    @Test
    void deleteCustomer_ShouldCallRepositoryDelete() {
        doNothing().when(customerRepository).deleteById(1L);

        customerService.deleteCustomer(1L);

        verify(customerRepository).deleteById(1L);
    }

    @Test
    void findByEmail_ShouldReturnCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEmailContainingIgnoreCase("test")).thenReturn(customers);

        List<Customer> result = customerService.findByEmail("test");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test@example.com", result.get(0).getEmail());
    }
} 