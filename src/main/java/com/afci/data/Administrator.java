package com.afci.data;

import java.io.Serializable;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Administrator extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    // Attributs
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;
    
    @OneToMany(mappedBy = "administrator", cascade = CascadeType.ALL)
    private Set<Book> books;

    @OneToMany(mappedBy = "administrator", cascade = CascadeType.ALL)
    private Set<Author> authors;

    @OneToMany(mappedBy = "administrator", cascade = CascadeType.ALL)
    private Set<Order> orders;
    
    // Constructeur
    public Administrator() {
    }

    // Getters et Setters
    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    // Méthodes
    public void getAuthorDetails(int authorId) {
        // Récupère les détails d'un auteur
    }

    public void updateAuthor(String authorData) {
        // Met à jour un auteur
    }

    public void deleteAuthor(int authorId) {
        // Supprime un auteur
    }

    public void addBook(String bookData) {
        // Ajoute un livre
    }

    public void updateBook(String bookData) {
        // Met à jour un livre
    }

    public void deleteBook(String bookId) {
        // Supprime un livre
    }

    public void getOrderDetails(int orderId) {
        // Récupère les détails d'une commande
    }

    @Override
    public boolean isAdmin() {
        return super.isAdmin();
    }

    // Méthode toString
    @Override
    public String toString() {
        return "Administrator{" +
                "id_user=" + getUserId() +
                ", username='" + getUsername() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", role=" + getRole() +
                '}';
    }

}