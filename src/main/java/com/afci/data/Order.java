package com.afci.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Inheritance(strategy = InheritanceType.JOINED)
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date de commande est obligatoire")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_date")
    private Date orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Positive(message = "Le total doit être positif")
    private Double total;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "administrator_id")
    private Administrator administrator;

    @ManyToMany
    @JoinTable(name = "order_books", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Book> books = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Constructeurs
    public Order() {
        this.orderDate = new Date();
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        if (customer != null && !customer.getCustomerOrders().contains(this)) {
            customer.getCustomerOrders().add(this);
        }
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
        if (administrator != null && !administrator.getManagedOrders().contains(this)) {
            administrator.getManagedOrders().add(this);
        }
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
        calculateTotal();
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Méthodes métier
    public void addBook(Book book) {
        if (book != null && book.isInStock()) {
            books.add(book);
            calculateTotal();
        }
    }

    public void removeBook(Book book) {
        if (book != null) {
            books.remove(book);
            calculateTotal();
        }
    }

    private void calculateTotal() {
        this.total = books.stream()
                .mapToDouble(Book::getPrice)
                .sum();
    }

    public void validateOrder() {
        if (status == OrderStatus.PENDING) {
            boolean allBooksAvailable = books.stream()
                    .allMatch(Book::isInStock);

            if (allBooksAvailable) {
                books.forEach(book -> book.decreaseStock(1));
                status = OrderStatus.VALIDATED;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Order))
            return false;
        Order order = (Order) o;
        return id != null && id.equals(order.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", status=" + status +
                ", total=" + total +
                ", customer=" + (customer != null ? customer.getUsername() : "null") +
                ", numberOfBooks=" + (books != null ? books.size() : 0) +
                '}';
    }

    public void setBasketId(Long basketId) {
        // TODO Auto-generated method stub

    }

    public void setCustomerId(Long customerId) {
        // TODO Auto-generated method stub

    }

}