package com.afci.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Basket implements Serializable {
    private static final long serialVersionUID = 1L;

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_basket;
    private float totalPrice;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<String> books = new HashSet<>();

    // Constructor
    public Basket() {
    }

    public Basket(Long id_basket, float totalPrice) {
        this.id_basket = id_basket;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public Long getId_basket() {
        return id_basket;
    }

    public void setId_basket(Long id_basket) {
        this.id_basket = id_basket;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Set<String> getBooks() {
        return books;
    }

    public void setBooks(Set<String> books) {
        this.books = books;
    }

    // Methods
    public void addBook(String bookData) {
        books.add(bookData);
        calculTotal();
    }

    public void deleteBook(String bookId) {
        books.remove(bookId);
        calculTotal();
    }

    public void removeBook(String bookId) {
        books.remove(bookId);
        calculTotal();
    }

    public void calculTotal() {
        // Assuming each book has a fixed price for simplicity
        float pricePerBook = 10.0f;
        this.totalPrice = books.size() * pricePerBook;
    }

    public void emptyCart() {
        books.clear();
        calculTotal();
    }

    @Override
    public String toString() {
        return "Basket{" +
                "id_basket=" + id_basket +
                ", totalPrice=" + totalPrice +
                ", books=" + books +
                '}';
    }
}
