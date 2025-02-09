package com.afci.data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "baskets")
public class Basket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(
        name = "basket_books",
        joinColumns = @JoinColumn(name = "basket_id"),
        inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> books = new HashSet<>();  // Set de livres dans le panier

    @Column(name = "total_price")
    private Double totalPrice = 0.0;

    @Column(name = "total_items")
    private Integer totalItems = 0;

    // Constructeurs, Getters et Setters
    public Basket() {
    }

    // Ajouter un livre au panier
    public void addBook(Book book) {
        if (book != null) {
            books.add(book);
            updateTotals();
        }
    }

    // Mettre à jour les totaux du panier
    private void updateTotals() {
        this.totalItems = books.size();
        this.totalPrice = books.stream()
            .mapToDouble(Book::getPrice)
            .sum();
    }

    // Getter pour les livres (retourne un Set<Book>)
    public Set<Book> getBooks() {
        return books;
    }

    // Setter pour les livres
    public void setBooks(Set<Book> books) {
        this.books = books;
        updateTotals();
    }

    // Getter pour le client
    public Customer getCustomer() {
        return customer;
    }

    // Setter pour le client
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // Autres méthodes...
    public Object getBasketId() {
		// TODO Auto-generated method stub
		return null;
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
