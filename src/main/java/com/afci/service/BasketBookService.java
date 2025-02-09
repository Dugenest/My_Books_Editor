package com.afci.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afci.data.Basket;
import com.afci.data.BasketBook;
import com.afci.data.BasketBookRepository;
import com.afci.data.BasketRepository;
import com.afci.data.Book;
import com.afci.data.BookRepository;

@Service
public class BasketBookService {
    @Autowired
    private BasketBookRepository basketBookRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BasketRepository basketRepository;

    public BasketBook addBookToBasket(Long basketId, Long bookId, int quantity) {
        Basket basket = basketRepository.findById(basketId)
                .orElseThrow(() -> new RuntimeException("Basket not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Vérifier si le livre existe déjà dans le panier
        Optional<BasketBook> existingBasketBook = basketBookRepository
                .findByBasketIdAndBookId(basketId, bookId);

        if (existingBasketBook.isPresent()) {
            BasketBook basketBook = existingBasketBook.get();
            basketBook.setQuantity(basketBook.getQuantity() + quantity);
            return basketBookRepository.save(basketBook);
        }

        BasketBook basketBook = new BasketBook();
        basketBook.setBasket(basket);
        basketBook.setBook(book);
        basketBook.setQuantity(quantity);

        return basketBookRepository.save(basketBook);
    }

    public void removeBookFromBasket(Long basketId, Long bookId) {
        BasketBook basketBook = basketBookRepository
                .findByBasketIdAndBookId(basketId, bookId)
                .orElseThrow(() -> new RuntimeException("BasketBook not found"));
        basketBookRepository.delete(basketBook);
    }

    public BasketBook updateQuantity(Long basketId, Long bookId, int newQuantity) {
        BasketBook basketBook = basketBookRepository
                .findByBasketIdAndBookId(basketId, bookId)
                .orElseThrow(() -> new RuntimeException("BasketBook not found"));
        basketBook.setQuantity(newQuantity);
        return basketBookRepository.save(basketBook);
    }

    public List<BasketBook> getBasketBooks(Long basketId) {
        return basketBookRepository.findByBasketId(basketId);
    }

    public double calculateBasketTotal(Long basketId) {
        List<BasketBook> basketBooks = basketBookRepository.findByBasketId(basketId);
        return basketBooks.stream()
                .mapToDouble(bb -> bb.getBook().getPrice() * bb.getQuantity())
                .sum();
    }

    public void clearBasket(Long basketId) {
        List<BasketBook> basketBooks = basketBookRepository.findByBasketId(basketId);
        basketBookRepository.deleteAll(basketBooks);
    }
}