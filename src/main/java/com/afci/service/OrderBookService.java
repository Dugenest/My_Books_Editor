package com.afci.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afci.data.Book;
import com.afci.data.BookRepository;
import com.afci.data.Order;
import com.afci.data.OrderBook;
import com.afci.data.OrderBookRepository;
import com.afci.data.OrderRepository;

@Service
public class OrderBookService {
    @Autowired
    private OrderBookRepository orderBookRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private OrderRepository orderRepository;

    public OrderBook addBookToOrder(Long orderId, Long bookId, int quantity) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));

        // Vérification si le livre existe déjà dans la commande
        Optional<OrderBook> existingOrderBook = orderBookRepository
            .findByOrderIdAndBookId(orderId, bookId);

        if (existingOrderBook.isPresent()) {
            OrderBook orderBook = existingOrderBook.get();
            orderBook.setQuantity(orderBook.getQuantity() + quantity);
            return orderBookRepository.save(orderBook);
        }

        OrderBook orderBook = new OrderBook();
        orderBook.setOrder(order);
        orderBook.setBook(book);
        orderBook.setQuantity(quantity);
        orderBook.setPrice(book.getPrice());

        return orderBookRepository.save(orderBook);
    }

    public void removeBookFromOrder(Long orderId, Long bookId) {
        OrderBook orderBook = orderBookRepository
            .findByOrderIdAndBookId(orderId, bookId)
            .orElseThrow(() -> new RuntimeException("OrderBook not found"));
        orderBookRepository.delete(orderBook);
    }

    public OrderBook updateQuantity(Long orderId, Long bookId, int newQuantity) {
        OrderBook orderBook = orderBookRepository
            .findByOrderIdAndBookId(orderId, bookId)
            .orElseThrow(() -> new RuntimeException("OrderBook not found"));
        orderBook.setQuantity(newQuantity);
        return orderBookRepository.save(orderBook);
    }

    public List<OrderBook> getOrderBooks(Long orderId) {
        return orderBookRepository.findByOrderId(orderId);
    }
}