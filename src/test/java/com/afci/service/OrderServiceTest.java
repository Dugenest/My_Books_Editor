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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.afci.data.Order;
import com.afci.data.OrderStatus;
import com.afci.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        order.setCustomerId(1L);
        order.setStatus(OrderStatus.PENDING);
    }

    @Test
    void getAllOrders_ShouldReturnAllOrders() {
        List<Order> orders = Arrays.asList(order);
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getOrderById_ShouldReturnOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> result = orderService.getOrderById(1L);

        assertTrue(result.isPresent());
        assertEquals(order.getId(), result.get().getId());
    }

    @Test
    void createOrder_ShouldReturnSavedOrder() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.createOrder(order);

        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
    }

    @Test
    void updateOrder_WhenExists_ShouldReturnUpdatedOrder() {
        when(orderRepository.existsById(1L)).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.updateOrder(order);

        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
    }

    @Test
    void updateOrder_WhenNotExists_ShouldThrowException() {
        when(orderRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> orderService.updateOrder(order));
    }

    @Test
    void deleteOrder_ShouldCallRepositoryDelete() {
        doNothing().when(orderRepository).deleteById(1L);

        orderService.deleteOrder(1L);

        verify(orderRepository).deleteById(1L);
    }

    @Test
    void findOrdersByUser_ShouldReturnUserOrders() {
        Page<Order> orderPage = new PageImpl<>(Arrays.asList(order));
        when(orderRepository.findByUserId(eq(1L), any(PageRequest.class))).thenReturn(orderPage);

        Page<Order> result = orderService.findOrdersByUser(1L, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void createOrderFromBasket_ShouldReturnNewOrder() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.createOrderFromBasket(1L, 1L);

        assertNotNull(result);
        assertEquals(OrderStatus.PENDING, result.getStatus());
    }

    @Test
    void updateOrderStatus_WhenExists_ShouldUpdateStatus() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.updateOrderStatus(1L, OrderStatus.COMPLETED);

        assertEquals(OrderStatus.COMPLETED, result.getStatus());
    }

    @Test
    void cancelOrder_WhenExists_ShouldCancelOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.cancelOrder(1L);

        assertEquals(OrderStatus.CANCELLED, result.getStatus());
    }
} 