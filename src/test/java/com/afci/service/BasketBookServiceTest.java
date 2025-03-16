package com.afci.service;

import static org.junit.jupiter.api.Assertions.*;
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

import com.afci.data.*;
import com.afci.repository.BasketBookRepository;
import com.afci.repository.BasketRepository;
import com.afci.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
public class BasketBookServiceTest {

    @Mock
    private BasketBookRepository basketBookRepository;
    
    @Mock
    private BookRepository bookRepository;
    
    @Mock
    private BasketRepository basketRepository;

    @InjectMocks
    private BasketBookService basketBookService;

    private Basket basket;
    private Book book;
    private BasketBook basketBook;

    @BeforeEach
    void setUp() {
        basket = new Basket();
        basket.setBasketId(1L);

        book = new Book();
        book.setId(1L);
        book.setPrice(29.99);

        basketBook = new BasketBook();
        basketBook.setBasket(basket);
        basketBook.setBook(book);
        basketBook.setQuantity(2);
    }

    @Test
    void addBookToBasket_WhenExistingBook_ShouldUpdateQuantity() {
        // Ajouter ces deux lignes pour configurer les mocks manquants
        when(basketRepository.findById(1L)).thenReturn(Optional.of(basket));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        
        // Vos mocks existants
        when(basketBookRepository.findByBasketIdAndBookId(1L, 1L)).thenReturn(Optional.of(basketBook));
        when(basketBookRepository.save(any(BasketBook.class))).thenReturn(basketBook);
    
        BasketBook result = basketBookService.addBookToBasket(1L, 1L, 1);
    
        assertNotNull(result);
        assertEquals(3, result.getQuantity());
        verify(basketBookRepository).save(any(BasketBook.class));
    }

    @Test
    void removeBookFromBasket_ShouldDeleteBasketBook() {
        when(basketBookRepository.findByBasketIdAndBookId(1L, 1L)).thenReturn(Optional.of(basketBook));
        doNothing().when(basketBookRepository).delete(any(BasketBook.class));

        assertDoesNotThrow(() -> basketBookService.removeBookFromBasket(1L, 1L));
        verify(basketBookRepository).delete(basketBook);
    }

    @Test
    void updateQuantity_ShouldUpdateBasketBook() {
        when(basketBookRepository.findByBasketIdAndBookId(1L, 1L)).thenReturn(Optional.of(basketBook));
        when(basketBookRepository.save(any(BasketBook.class))).thenReturn(basketBook);

        BasketBook result = basketBookService.updateQuantity(1L, 1L, 5);

        assertNotNull(result);
        assertEquals(5, result.getQuantity());
    }

    @Test
    void getBasketBooks_ShouldReturnList() {
        when(basketBookRepository.findByBasketId(1L)).thenReturn(Arrays.asList(basketBook));

        List<BasketBook> result = basketBookService.getBasketBooks(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void calculateBasketTotal_ShouldReturnTotal() {
        when(basketBookRepository.findByBasketId(1L)).thenReturn(Arrays.asList(basketBook));

        double result = basketBookService.calculateBasketTotal(1L);

        assertEquals(59.98, result, 0.01); // 29.99 * 2
    }

    @Test
    void clearBasket_ShouldDeleteAllBasketBooks() {
        List<BasketBook> basketBooks = Arrays.asList(basketBook);
        when(basketBookRepository.findByBasketId(1L)).thenReturn(basketBooks);
        doNothing().when(basketBookRepository).deleteAll(basketBooks);

        assertDoesNotThrow(() -> basketBookService.clearBasket(1L));
        verify(basketBookRepository).deleteAll(basketBooks);
    }
} 