package com.afci.data;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "wishlists")
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
        name = "wishlist_books",
        joinColumns = @JoinColumn(name = "wishlist_id"),
        inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> books = new HashSet<>();

    // Constructeurs
    public Wishlist() {
    }

    public Wishlist(User user) {
        this.user = user;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    // Méthodes utilitaires
    public void addBook(Book book) {
        if (book != null) {
            books.add(book);
        }
    }

    public void removeBook(Book book) {
        if (book != null) {
            books.remove(book);
        }
    }

    @Override
    public String toString() {
        return "Wishlist{" +
               "id=" + id +
               ", userId=" + (user != null ? user.getId() : "null") +
               ", numberOfBooks=" + (books != null ? books.size() : 0) +
               '}';
    }
} 