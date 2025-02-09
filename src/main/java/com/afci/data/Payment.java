package com.afci.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date de paiement est obligatoire")
    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Positive(message = "Le montant doit être positif")
    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "transaction_id")
    private String transactionId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")  // Assurez-vous que le champ de jointure correspond
    private Customer customer;

    // Constructeur sans paramètres
    public Payment() {
    }

    // Constructeur avec paramètres
    public Payment(LocalDateTime paymentDate, Double amount, PaymentStatus status, PaymentMethod paymentMethod, String transactionId, Order order) {
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.order = order;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    // Méthode toString
    @Override
    public String toString() {
        return "Payment{" +
               "id=" + id +
               ", paymentDate=" + paymentDate +
               ", amount=" + amount +
               ", status=" + status +
               ", paymentMethod=" + paymentMethod +
               ", transactionId='" + transactionId + '\'' +
               ", order=" + (order != null ? order.getId() : "null") +  // Protection contre null pour éviter NPE
               '}';
    }
}
