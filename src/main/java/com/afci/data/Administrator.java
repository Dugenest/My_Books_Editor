package com.afci.data;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "administrators")
@PrimaryKeyJoinColumn(name = "user_id")
@DiscriminatorValue("ADMIN")
public class Administrator extends User {

    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "administrator", cascade = CascadeType.ALL)
    private Set<Book> managedBooks = new HashSet<>();

    @OneToMany(mappedBy = "administrator", cascade = CascadeType.ALL)
    private Set<Order> managedOrders = new HashSet<>();

    // Constructeurs
    public Administrator() {
        super();
    }

    public Administrator(String username, String password, String email) {
        super(username, password, email);
    }

    // Getters et Setters
    public Set<Book> getManagedBooks() { return managedBooks; }
    public void setManagedBooks(Set<Book> books) {
        this.managedBooks = books;
        if (books != null) {
            books.forEach(book -> book.setAdministrator(this));
        }
    }

    public Set<Order> getManagedOrders() { return managedOrders; }
    public void setManagedOrders(Set<Order> orders) {
        this.managedOrders = orders;
        if (orders != null) {
            orders.forEach(order -> order.setAdministrator(this));
        }
    }

    // Méthodes métier
    public void validateOrder(Order order) {
        if (order != null) {
            order.setStatus(OrderStatus.VALIDATED);
            order.setAdministrator(this);
            managedOrders.add(order);
        }
    }

    public void addBook(Book book) {
        if (book != null) {
            managedBooks.add(book);
            book.setAdministrator(this);
        }
    }

    public void removeBook(Book book) {
        if (book != null) {
            managedBooks.remove(book);
            if (book.getAdministrator() == this) {
                book.setAdministrator(null);
            }
        }
    }

    @Override
    public String toString() {
        return "Administrator{" +
               "id=" + getId() +
               ", username='" + getUsername() + '\'' +
               ", email='" + getEmail() + '\'' +
               ", managedBooks=" + (managedBooks != null ? managedBooks.size() : 0) +
               ", managedOrders=" + (managedOrders != null ? managedOrders.size() : 0) +
               '}';
    }
}