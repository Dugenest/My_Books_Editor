package com.afci.data;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "baskets")
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BasketBook> basketBooks = new HashSet<>();

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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Book> getBooks() {
        Set<Book> books = new HashSet<>();
        for (BasketBook basketBook : basketBooks) {
            books.add(basketBook.getBook());
        }
        return books;
    }

    public void setBooks(Set<Book> books) {
        basketBooks.clear();
        
        if (books != null) {
            for (Book book : books) {
                addBook(book);
            }
        }
        updateTotals();
    }

    public Set<BasketBook> getBasketBooks() {
        return basketBooks;
    }

    public void setBasketBooks(Set<BasketBook> basketBooks) {
        this.basketBooks = basketBooks;
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
            BasketBook existingBasketBook = null;
            for (BasketBook basketBook : basketBooks) {
                if (basketBook.getBook().equals(book)) {
                    existingBasketBook = basketBook;
                    break;
                }
            }
            
            if (existingBasketBook != null) {
                existingBasketBook.setQuantity(existingBasketBook.getQuantity() + 1);
            } else {
                BasketBook basketBook = new BasketBook(this, book, 1);
                basketBooks.add(basketBook);
            }
            updateTotals();
        }
    }

    private void updateTotals() {
        this.totalItems = 0;
        this.totalPrice = 0.0;
        
        for (BasketBook basketBook : basketBooks) {
            this.totalItems += basketBook.getQuantity();
            this.totalPrice += basketBook.getBook().getPrice() * basketBook.getQuantity();
        }
    }

    @Override
    public String toString() {
        return "Basket{" +
            "id=" + id +
            ", customer=" + (customer != null ? customer.getUsername() : "null") +
            ", totalItems=" + totalItems +
            ", totalPrice=" + totalPrice +
            '}';
    }
}