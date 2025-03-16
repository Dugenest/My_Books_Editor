package com.afci.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.afci.data.Customer;
import com.afci.data.Payment;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByOrderId(Long orderId);
    List<Payment> findByCustomerId(Long customerId);
    List<Payment> findByCustomer(Customer customer);

}
