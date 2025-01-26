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
public class Order_details extends Order implements Serializable {
    private static final long serialVersionUID = 1L;

    // Attributs
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_order_details;
    private String details;

    @OneToMany(mappedBy = "order_details", cascade = CascadeType.ALL)
    private Set<Customer> customers;

    @OneToMany(mappedBy = "order_details", cascade = CascadeType.ALL)
    private Set<Book> books;

    // Constructeur
    public Order_details() {
    }

    public Order_details(int id_order_details, String details) {
        this.id_order_details = id_order_details;
        this.details = details;
    }

    // Getters et Setters
    public int getId_order_details() {
        return id_order_details;
    }

    public void setId_order_details(int id_order_details) {
        this.id_order_details = id_order_details;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Order_details{" +
                "id_order_details=" + id_order_details +
                ", details='" + details + '\'' +
                ", books=" + books +
                ", customers=" + customers +
                '}';
    }
}