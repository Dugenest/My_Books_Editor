package com.afci.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.afci.data.Basket;
import com.afci.data.Customer;

import java.util.List;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {
    List<Basket> findByCustomer(Customer customer);
}