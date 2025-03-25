package com.afci.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.afci.data.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUserId(Long userId, Pageable pageable);

    long countByUserId(Long userId);

    @Query("SELECT COUNT(ob.book) FROM OrderBook ob WHERE ob.order.user.id = :userId")
    long countBooksByUserId(@Param("userId") Long userId);
}