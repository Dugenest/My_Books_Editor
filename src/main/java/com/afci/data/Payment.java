package com.afci.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;

    // Attributs
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_payment;
    private float totalPrice;
    private String paymentMethod;
    private Date payment_date;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    private Set<Order> orders;

    // Constructeur
    public Payment() {
    }

    public Payment(Long id_payment, float totalPrice, String paymentMethod, Date payment_date) {
        this.id_payment = id_payment;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.payment_date = payment_date;
    }

    // Getters et Setters
    public Long getId_payment() {
        return id_payment;
    }

    public void setId_payment(Long id_payment) {
        this.id_payment = id_payment;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(Date payment_date) {
        this.payment_date = payment_date;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    // Méthodes
    public void makePayment() {
        // Logique pour effectuer le paiement
        System.out.println("Payment made successfully.");
    }

    public void cancelPayment() {
        // Logique pour annuler le paiement
        System.out.println("Payment cancelled successfully.");
    }

    public void generateReceipt() {
        // Logique pour générer le reçu
        System.out.println("Receipt generated successfully.");
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id_payment=" + id_payment +
                ", totalPrice=" + totalPrice +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", payment_date=" + payment_date +
                '}';
    }

    
}