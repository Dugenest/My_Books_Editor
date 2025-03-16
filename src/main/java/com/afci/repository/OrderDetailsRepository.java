package com.afci.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.afci.data.OrderDetails;

@Repository

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {

}
