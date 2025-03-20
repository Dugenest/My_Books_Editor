package com.afci.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.afci.data.Basket;
import com.afci.data.Book;
import com.afci.service.BasketService;

@ExtendWith(MockitoExtension.class)
public class BasketControllerTest {

    @Mock
    private BasketService basketService;

    @InjectMocks
    private BasketController basketController;

    private Basket basket;
    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");

        basket = new Basket();
        basket.setId(1L);
        Set<Book> books = new HashSet<>();
        books.add(book);
        basket.setBooks(books);
    }

    @Test
    void createBasket_ShouldReturnCreatedBasket() {
        when(basketService.createBasket(any(Basket.class))).thenReturn(basket);

        ResponseEntity<Basket> response = basketController.createBasket(basket);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Basket responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(basket.getId(), responseBody.getId());
    }

    @Test
    void getBasketById_WhenExists_ShouldReturnBasket() {
        when(basketService.getBasketById(1L)).thenReturn(Optional.of(basket));

        ResponseEntity<Basket> response = basketController.getBasketById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Basket responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(basket.getId(), responseBody.getId());
    }

    @Test
    void getBasketById_WhenNotExists_ShouldReturnNotFound() {
        when(basketService.getBasketById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Basket> response = basketController.getBasketById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void addBookToBasket_WhenSuccess_ShouldReturnUpdatedBasket() {
        when(basketService.addToBasket(eq(1L), any(Basket.class))).thenReturn(basket);

        ResponseEntity<Basket> response = basketController.addBookToBasket(1L, basket);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Basket responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(basket.getId(), responseBody.getId());
    }

    @Test
    void deleteBasket_ShouldReturnOk() {
        doNothing().when(basketService).deleteBasket(1L);

        ResponseEntity<Void> response = basketController.deleteBasket(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
} 