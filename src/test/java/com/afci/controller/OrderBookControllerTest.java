package com.afci.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.afci.data.Book;
import com.afci.data.Order;
import com.afci.data.OrderBook;
import com.afci.service.OrderBookService;

@ExtendWith(MockitoExtension.class)
public class OrderBookControllerTest {

    @Mock
    private OrderBookService orderBookService;

    @InjectMocks
    private OrderBookController orderBookController;

    private OrderBook orderBook;
    private Order order;
    private Book book;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);

        book = new Book();
        book.setId(1L);
        book.setPrice(29.99);

        orderBook = new OrderBook();
        orderBook.setOrder(order);
        orderBook.setBook(book);
        orderBook.setQuantity(1);
        orderBook.setPrice(book.getPrice());
    }

    @Test
    void addBookToOrder_ShouldReturnOrderBook() {
        when(orderBookService.addBookToOrder(eq(1L), eq(1L), eq(1))).thenReturn(orderBook);

        ResponseEntity<OrderBook> response = orderBookController.addBookToOrder(1L, 1L, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getQuantity());
    }

    @Test
    void removeBookFromOrder_ShouldReturnNoContent() {
        doNothing().when(orderBookService).removeBookFromOrder(1L, 1L);

        ResponseEntity<Void> response = orderBookController.removeBookFromOrder(1L, 1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void updateQuantity_ShouldReturnUpdatedOrderBook() {
        when(orderBookService.updateQuantity(eq(1L), eq(1L), eq(5))).thenReturn(orderBook);

        ResponseEntity<OrderBook> response = orderBookController.updateQuantity(1L, 1L, 5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getOrderBooks_ShouldReturnList() {
        List<OrderBook> orderBooks = Arrays.asList(orderBook);
        when(orderBookService.getOrderBooks(1L)).thenReturn(orderBooks);

        ResponseEntity<List<OrderBook>> response = orderBookController.getOrderBooks(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }
} 