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
    void addBookToBasket_ShouldAddNewBookToBasket() {
        when(basketRepository.findById(1L)).thenReturn(Optional.of(basket));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(basketBookRepository.findByBasketIdAndBookId(1L, 1L)).thenReturn(Optional.empty());
        when(basketBookRepository.save(any(BasketBook.class))).thenReturn(basketBook);

        BasketBook result = basketBookService.addBookToBasket(1L, 1L, 2);

        assertNotNull(result);
        assertEquals(2, result.getQuantity());
        assertEquals(basket, result.getBasket());
        assertEquals(book, result.getBook());
    }

    @Test
    void addBookToBasket_ShouldUpdateQuantityIfBookAlreadyInBasket() {
        when(basketRepository.findById(1L)).thenReturn(Optional.of(basket));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(basketBookRepository.findByBasketIdAndBookId(1L, 1L)).thenReturn(Optional.of(basketBook));
        when(basketBookRepository.save(any(BasketBook.class))).thenReturn(basketBook);

        BasketBook result = basketBookService.addBookToBasket(1L, 1L, 3);

        assertNotNull(result);
        assertEquals(5, result.getQuantity()); // 2 (initial) + 3 (added)
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
    void getBasketBooks_ShouldReturnAllBooksInBasket() {
        when(basketBookRepository.findByBasketId(1L)).thenReturn(Arrays.asList(basketBook));

        List<BasketBook> result = basketBookService.getBasketBooks(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(basketBook, result.get(0));
    }

    @Test
    void calculateBasketTotal_ShouldReturnCorrectTotal() {
        when(basketBookRepository.findByBasketId(1L)).thenReturn(Arrays.asList(basketBook));

        double result = basketBookService.calculateBasketTotal(1L);

        assertEquals(59.98, result, 0.01); // 29.99 * 2
    }

    @Test
    void clearBasket_ShouldRemoveAllBooksFromBasket() {
        when(basketBookRepository.findByBasketId(1L)).thenReturn(Arrays.asList(basketBook));

        basketBookService.clearBasket(1L);

        verify(basketBookRepository, times(1)).deleteAll(anyList());
    }
} 