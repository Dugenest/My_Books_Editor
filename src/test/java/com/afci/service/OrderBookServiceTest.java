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

import com.afci.data.Book;
import com.afci.data.Order;
import com.afci.data.OrderBook;
import com.afci.repository.BookRepository;
import com.afci.repository.OrderBookRepository;
import com.afci.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
public class OrderBookServiceTest {

    @Mock
    private OrderBookRepository orderBookRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderBookService orderBookService;

    private Order order;
    private Book book;
    private OrderBook orderBook;

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
    void addBookToOrder_NewBook_ShouldCreateOrderBook() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(orderBookRepository.findByOrderIdAndBookId(1L, 1L)).thenReturn(Optional.empty());
        when(orderBookRepository.save(any(OrderBook.class))).thenReturn(orderBook);

        OrderBook result = orderBookService.addBookToOrder(1L, 1L, 1);

        assertNotNull(result);
        assertEquals(1, result.getQuantity());
        assertEquals(book.getPrice(), result.getPrice());
    }

    @Test
    void addBookToOrder_ExistingBook_ShouldUpdateQuantity() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(orderBookRepository.findByOrderIdAndBookId(1L, 1L)).thenReturn(Optional.of(orderBook));
        when(orderBookRepository.save(any(OrderBook.class))).thenReturn(orderBook);

        OrderBook result = orderBookService.addBookToOrder(1L, 1L, 2);

        assertNotNull(result);
        assertEquals(3, result.getQuantity()); // 1 existant + 2 ajoutés
    }

    @Test
    void removeBookFromOrder_ShouldDeleteOrderBook() {
        when(orderBookRepository.findByOrderIdAndBookId(1L, 1L)).thenReturn(Optional.of(orderBook));
        doNothing().when(orderBookRepository).delete(orderBook);

        assertDoesNotThrow(() -> orderBookService.removeBookFromOrder(1L, 1L));
        verify(orderBookRepository).delete(orderBook);
    }

    @Test
    void updateQuantity_ShouldUpdateAndSave() {
        when(orderBookRepository.findByOrderIdAndBookId(1L, 1L)).thenReturn(Optional.of(orderBook));
        when(orderBookRepository.save(any(OrderBook.class))).thenReturn(orderBook);

        OrderBook result = orderBookService.updateQuantity(1L, 1L, 5);

        assertNotNull(result);
        assertEquals(5, result.getQuantity());
    }

    @Test
    void getOrderBooks_ShouldReturnList() {
        List<OrderBook> orderBooks = Arrays.asList(orderBook);
        when(orderBookRepository.findByOrderId(1L)).thenReturn(orderBooks);

        List<OrderBook> result = orderBookService.getOrderBooks(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
} 