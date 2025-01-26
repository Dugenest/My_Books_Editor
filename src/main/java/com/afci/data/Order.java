package com.afci.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_order;
    private Date order_date;
    private int order_status;
    private double total;
    private String client;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<Order_details> order_details;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToMany
    @JoinTable(
        name = "order_books",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> books;

    // Constructor
    public Order() {
    }

    public Order(Long id_order, Date order_date, int order_status, 
                double total, String client) {
        this.id_order = id_order;
        this.order_date = order_date;
        this.order_status = order_status;
        this.total = total;
        this.client = client;
    }

    // Getters and Setters
    public Long getId_order() {
        return id_order;
    }

    public void setId_order(Long id_order) {
        this.id_order = id_order;
    }

    public Date getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Date order_date) {
        this.order_date = order_date;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    // Methods
    public void getOrderDetails(Long orderId) {
        // Retrieve order details
        System.out.println("Getting details for order ID: " + orderId);
    }

    public void updateOrderStatus(int status) {
        // Update order status
        this.order_status = status;
        System.out.println("Order status updated to: " + status);
    }

    public void deleteOrder(Long orderId) {
        // Delete an order
        System.out.println("Order with ID " + orderId + " has been deleted.");
    }

    public void calculTotal() {
        // Calculate the total of the order
        System.out.println("Calculating total for order ID: " + id_order);
    }

    public void changeStatus(String status) {
        // Change the status of the order
        System.out.println("Changing status to: " + status);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id_order=" + id_order +
                ", order_date=" + order_date +
                ", order_status=" + order_status +
                ", total=" + total +
                ", client='" + client + '\'' +
                '}';
    }
}