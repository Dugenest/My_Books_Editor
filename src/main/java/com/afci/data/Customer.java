package com.afci.data;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customers")
@PrimaryKeyJoinColumn(name = "user_id")
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {

    private static final long serialVersionUID = 1L;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Basket basket;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Order> customerOrders = new HashSet<>();

    // Constructeurs
    public Customer() {
        super();
    }

    public Customer(String username, String password, String email) {
        super(username, password, email);
    }

    // Getters et Setters
    public Basket getBasket() { 
        return basket; 
    }

    public void setBasket(Basket basket) {
        if (this.basket != basket) {
            Basket oldBasket = this.basket;
            this.basket = basket;
            
            if (oldBasket != null) {
                oldBasket.setCustomer(null);
            }
            
            if (basket != null && basket.getCustomer() != this) {
                basket.setCustomer(this);
            }
        }
    }

    public Set<Comment> getComments() { 
        return comments; 
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
        if (comments != null) {
            comments.forEach(comment -> comment.setCustomer(this));
        }
    }

    public Set<Order> getCustomerOrders() { 
        return customerOrders; 
    }

    public void setCustomerOrders(Set<Order> orders) {
        this.customerOrders = orders;
        if (orders != null) {
            orders.forEach(order -> order.setCustomer(this));
        }
    }

    // Méthodes utilitaires
    public void addComment(Comment comment) {
        if (comment != null) {
            comments.add(comment);
            comment.setCustomer(this);
        }
    }

    public void removeComment(Comment comment) {
        if (comment != null) {
            comments.remove(comment);
            if (comment.getCustomer() == this) {
                comment.setCustomer(null);
            }
        }
    }

    public void addOrder(Order order) {
        if (order != null) {
            customerOrders.add(order);
            order.setCustomer(this);
        }
    }

    public void removeOrder(Order order) {
        if (order != null) {
            customerOrders.remove(order);
            if (order.getCustomer() == this) {
                order.setCustomer(null);
            }
        }
    }

    // Méthodes métier
    public void createBasket() {
        if (this.basket == null) {
            this.basket = new Basket();
            this.basket.setCustomer(this);
        }
    }

    @Override
    public String toString() {
        return "Customer{" +
               "id=" + getId() +
               ", username='" + getUsername() + '\'' +
               ", email='" + getEmail() + '\'' +
               ", hasBasket=" + (basket != null) +
               ", numberOfComments=" + (comments != null ? comments.size() : 0) +
               ", numberOfOrders=" + (customerOrders != null ? customerOrders.size() : 0) +
               '}';
    }
}
