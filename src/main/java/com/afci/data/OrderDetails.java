package com.afci.data;

import jakarta.persistence.*;

@Entity
@PrimaryKeyJoinColumn(name = "order_id") // Correct si Order est la classe parent
@DiscriminatorValue("ORDER_DETAIL") // Utile pour la stratégie SINGLE_TABLE
public class OrderDetails extends Order {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String details;
    private int quantity;
    private double unitPrice;

    // Constructors
    public OrderDetails() {}

    public OrderDetails(String details, int quantity, double unitPrice) {
        this.details = details;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getters and Setters
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    @Override
    public String toString() {
        return "OrderDetails{" +
               "orderId=" + super.getId() +
               ", details='" + details + '\'' +
               ", quantity=" + quantity +
               ", unitPrice=" + unitPrice +
               ", total=" + super.getTotal() +
               ", orderDate=" + super.getOrderDate() +
               '}';
    }
}