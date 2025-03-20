package com.afci.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

import com.afci.data.Basket;
import com.afci.data.BasketBook;
import com.afci.data.Book;
import com.afci.service.BasketBookService;

@ExtendWith(MockitoExtension.class)
public class BasketBookControllerTest {

    @Mock
    private BasketBookService basketBookService;

    @InjectMocks
    private BasketBookController basketBookController;

    private BasketBook basketBook;
    private Basket basket;
    private Book book;

    @BeforeEach
    void setUp() {
        basket = new Basket();
        basket.setId(1L);

        book = new Book();
        book.setId(1L);
        book.setPrice(29.99);

        basketBook = new BasketBook();
        basketBook.setBasket(basket);
        basketBook.setBook(book);
        basketBook.setQuantity(2);
    }

    @Test
    void addBookToBasket_ShouldReturnBasketBook() {
        when(basketBookService.addBookToBasket(1L, 1L, 2)).thenReturn(basketBook);

        ResponseEntity<BasketBook> response = basketBookController.addBookToBasket(1L, 1L, 2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        BasketBook responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.getQuantity());
    }

    @Test
    void removeBookFromBasket_ShouldReturnNoContent() {
        doNothing().when(basketBookService).removeBookFromBasket(1L, 1L);

        ResponseEntity<Void> response = 
            basketBookController.removeBookFromBasket(1L, 1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void updateQuantity_ShouldReturnUpdatedBasketBook() {
        when(basketBookService.updateQuantity(1L, 1L, 5)).thenReturn(basketBook);

        ResponseEntity<BasketBook> response = basketBookController.updateQuantity(1L, 1L, 5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        BasketBook responseBody = response.getBody();
        assertNotNull(responseBody);
    }

    @Test
    void getBasketBooks_ShouldReturnList() {
        when(basketBookService.getBasketBooks(1L)).thenReturn(Arrays.asList(basketBook));

        ResponseEntity<List<BasketBook>> response = basketBookController.getBasketBooks(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<BasketBook> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.size());
    }

    @Test
    void getBasketTotal_ShouldReturnTotal() {
        when(basketBookService.calculateBasketTotal(1L)).thenReturn(59.98);

        ResponseEntity<Double> response = basketBookController.getBasketTotal(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Double responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(59.98, responseBody, 0.01);
    }

    @Test
    void clearBasket_ShouldReturnNoContent() {
        doNothing().when(basketBookService).clearBasket(1L);

        ResponseEntity<Void> response = 
            basketBookController.clearBasket(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
} 