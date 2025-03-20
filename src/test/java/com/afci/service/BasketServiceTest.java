package com.afci.service;

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

import com.afci.data.Basket;
import com.afci.data.Book;
import com.afci.repository.BasketRepository;

@ExtendWith(MockitoExtension.class)
public class BasketServiceTest {

    @Mock
    private BasketRepository basketRepository;

    @InjectMocks
    private BasketService basketService;

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
        when(basketRepository.save(any(Basket.class))).thenReturn(basket);

        Basket result = basketService.createBasket(basket);

        assertNotNull(result);
        assertEquals(basket.getId(), result.getId());
    }

    @Test
    void getBasketById_ShouldReturnBasket() {
        when(basketRepository.findById(1L)).thenReturn(Optional.of(basket));

        Optional<Basket> result = basketService.getBasketById(1L);

        assertTrue(result.isPresent());
        assertEquals(basket.getId(), result.get().getId());
    }

    @Test
    void addToBasket_WhenBasketExistsAndHasBooks_ShouldAddBook() {
        when(basketRepository.findById(1L)).thenReturn(Optional.of(basket));
        when(basketRepository.save(any(Basket.class))).thenReturn(basket);

        Basket itemToAdd = new Basket();
        Set<Book> books = new HashSet<>();
        Book newBook = new Book();
        newBook.setId(2L);
        books.add(newBook);
        itemToAdd.setBooks(books);

        Basket result = basketService.addToBasket(1L, itemToAdd);

        assertNotNull(result);
        verify(basketRepository).save(any(Basket.class));
    }

    @Test
    void addToBasket_WhenBasketNotFound_ShouldThrowException() {
        when(basketRepository.findById(1L)).thenReturn(Optional.empty());

        Basket itemToAdd = new Basket();
        Set<Book> books = new HashSet<>();
        books.add(book);
        itemToAdd.setBooks(books);

        assertThrows(RuntimeException.class, () -> basketService.addToBasket(1L, itemToAdd));
    }

    @Test
    void addToBasket_WhenNoBooks_ShouldThrowException() {
        when(basketRepository.findById(1L)).thenReturn(Optional.of(basket));

        Basket itemToAdd = new Basket();
        itemToAdd.setBooks(new HashSet<>());

        assertThrows(RuntimeException.class, () -> basketService.addToBasket(1L, itemToAdd));
    }

    @Test
    void deleteBasket_ShouldCallRepositoryDelete() {
        doNothing().when(basketRepository).deleteById(1L);

        basketService.deleteBasket(1L);

        verify(basketRepository).deleteById(1L);
    }
} 