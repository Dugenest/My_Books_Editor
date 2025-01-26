package com.afci.data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Customer extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    // Attributs
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCustomer;    
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Order> orders;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Basket basket;

    // Constructeur
    public Customer() {
    }

    public Customer(Long idCustomer, Long userId, String username, String password, int role, 
                   Set<Order> orders, Basket basket) {
        super(userId, username, password, role);
        this.idCustomer = idCustomer;
        this.orders = orders;
        this.basket = basket;
    }

    // Getters et Setters
    public Long getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(Long idCustomer) {
        this.idCustomer = idCustomer;
    }

    @SuppressWarnings("unchecked")
    public List<Order> getOrders() { return (List<Order>) orders; }
    @SuppressWarnings("unchecked")
    public void setOrders(List<Order> orders) { this.orders = (Set<Order>) orders; }
    
    public Basket getBasket() { return basket; }
    public void setBasket(Basket basket) { this.basket = basket; }

    // Méthodes
    public void addToOrder(Order order) {
        orders.add(order);
    }

    @SuppressWarnings("unchecked")
    public Order getToOrder(int index) {
        if (index >= 0 && index < orders.size()) {
            return ((List<Order>) orders).get(index);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Order> orderHistory() {
        return (List<Order>) orders;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "orders=" + orders +
                ", basket=" + basket +
                ", id_user=" + getUserId() +
                ", username='" + getUsername() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", role=" + getRole() +
                '}';
    }
}