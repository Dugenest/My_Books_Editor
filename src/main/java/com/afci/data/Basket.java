package com.afci.data;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "baskets")
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long basketId;

    @ManyToMany
    @JoinTable(
        name = "basket_books",
        joinColumns = @JoinColumn(name = "basket_id"),
        inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> books = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "total_price")
    private Double totalPrice = 0.0;

    @Column(name = "total_items")
    private Integer totalItems = 0;

    // Constructeurs
    public Basket() {
    }

    public Basket(Customer customer) {
        this.customer = customer;
    }

    // Getters et Setters
    public Long getBasketId() {
        return basketId;
    }

    public void setBasketId(Long basketId) {
        this.basketId = basketId;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
        updateTotals();
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    // Méthodes métier
    public void addBook(Book book) {
        if (book != null) {
            books.add(book);
            updateTotals();
        }
    }

    private void updateTotals() {
        this.totalItems = books.size();
        this.totalPrice = books.stream()
            .mapToDouble(Book::getPrice)
            .sum();
    }

    @Override
    public String toString() {
        return "Basket{" +
            "basketId=" + basketId +
            ", customer=" + (customer != null ? customer.getUsername() : "null") +
            ", totalItems=" + totalItems +
            ", totalPrice=" + totalPrice +
            '}';
    }
}