package com.afci.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "serials")
public class Serial implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title")
    private String title;  // Si vous voulez que ce titre vienne de la classe Book, pensez à un getter sur Book.

    @NotBlank(message = "Le numéro de série est obligatoire")
    @Column(unique = true, nullable = false)
    private String serialNumber;

    @NotNull(message = "La date de création est obligatoire")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date")
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "activation_date")
    private Date activationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(nullable = false)
    private boolean active = false;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    // Constructeurs
    public Serial() {
        this.creationDate = new Date();
    }

    public Serial(String serialNumber, Book book) {
        this();
        this.serialNumber = serialNumber;
        this.book = book;
        this.title = book != null ? book.getTitle() : null;  // Assure que le title est mis à jour depuis Book
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }

    public Date getActivationDate() { return activationDate; }
    public void setActivationDate(Date activationDate) { this.activationDate = activationDate; }

    public Date getExpirationDate() { return expirationDate; }
    public void setExpirationDate(Date expirationDate) { this.expirationDate = expirationDate; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Book getBook() { return book; }
    public void setBook(Book book) { 
        this.book = book; 
        this.title = book != null ? book.getTitle() : null;  // Mise à jour du title quand Book change
    }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    // Méthodes métier
    public boolean activate(Customer customer) {
        if (!active && customer != null) {
            this.customer = customer;
            this.active = true;
            this.activationDate = new Date();
            // Par exemple, validité de 1 an
            this.expirationDate = new Date(activationDate.getTime() + 365L * 24 * 60 * 60 * 1000);
            return true;
        }
        return false;
    }

    public boolean isValid() {
        if (!active) return false;
        Date now = new Date();
        return now.after(activationDate) && now.before(expirationDate);
    }

    public void deactivate() {
        this.active = false;
        this.expirationDate = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Serial)) return false;
        Serial serial = (Serial) o;
        return id != null && id.equals(serial.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Serial{" +
               "id=" + id +
               ", serialNumber='" + serialNumber + '\'' +
               ", creationDate=" + creationDate +
               ", activationDate=" + activationDate +
               ", expirationDate=" + expirationDate +
               ", active=" + active +
               ", book=" + (book != null ? book.getTitle() : "null") +
               ", customer=" + (customer != null ? customer.getUsername() : "null") +
               '}';
    }
}
