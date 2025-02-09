package com.afci.data;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_books")
public class OrderBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double price; // Prix au moment de la commande

    // Constructeurs
    public OrderBook() {
    }

    public OrderBook(Order order, Book book, Integer quantity, Double price) {
        this.order = order;
        this.book = book;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    // Méthode utilitaire pour calculer le sous-total
    public Double getSubTotal() {
        return price * quantity;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderBook orderBook = (OrderBook) o;
        return Objects.equals(id, orderBook.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OrderBook{")
          .append("id=").append(id)
          .append(", orderId=").append(order != null ? order.getId() : "null")
          .append(", bookId=").append(book != null ? book.getId() : "null")
          .append(", bookTitle=").append(book != null ? book.getTitle() : "null")
          .append(", quantity=").append(quantity)
          .append(", price=").append(price)
          .append(", subTotal=").append(getSubTotal())
          .append('}');
        return sb.toString();
    }
}