package com.afci.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.afci.data.Order;
import com.afci.data.OrderStatus;
import com.afci.repository.OrderRepository;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order updateOrder(Order order) {
        if (orderRepository.existsById(order.getId())) {
            return orderRepository.save(order);
        }
        throw new RuntimeException("Commande non trouvée avec l'ID : " + order.getId());
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public List<Order> findOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getCustomerOrders(Long customerId) {
        return orderRepository.findByUserId(customerId);
    }

    public Order createOrderFromBasket(Long customerId, Long basketId) {
        Order newOrder = new Order();
        newOrder.setCustomerId(customerId);  
        newOrder.setBasketId(basketId);      
        newOrder.setStatus(OrderStatus.PENDING);
        return orderRepository.save(newOrder);
    }

    public Order updateOrderStatus(Long id, OrderStatus status) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(status);
            return orderRepository.save(order);
        } else {
            throw new RuntimeException("Commande non trouvée avec l'ID : " + id);
        }
    }

    public Order cancelOrder(Long id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(OrderStatus.CANCELLED);
            return orderRepository.save(order);
        } else {
            throw new RuntimeException("Commande non trouvée avec l'ID : " + id);
        }
    }

}