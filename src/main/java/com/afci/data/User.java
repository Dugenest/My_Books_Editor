package com.afci.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING, length = 31)
@DiscriminatorValue("USER")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit contenir entre 3 et 50 caractères")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "Le mot de passe est obligatoire", groups = { CreateValidation.class })
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères", groups = { CreateValidation.class })
    private String password;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "subscribed_to_newsletter")
    private boolean subscribedToNewsletter;

    private String phone;
    private String address;
    private String role;

    @Column(name = "registration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    @Column(name = "active")
    private boolean active = true; // Renommé le champ et gardé la valeur par défaut

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Book> books = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Order> orders = new HashSet<>();

    // Constructeurs
    public User() {
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isSubscribedToNewsletter() {
        return subscribedToNewsletter;
    }

    public void setSubscribedToNewsletter(boolean subscribedToNewsletter) {
        this.subscribedToNewsletter = subscribedToNewsletter;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    } // Le getter suivant la convention JavaBean

    public void setActive(boolean active) {
        this.active = active;
    } // Le setter correspondant

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
        if (books != null) {
            books.forEach(book -> book.setUser(this));
        }
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
        if (orders != null) {
            orders.forEach(order -> order.setUser(this));
        }
    }

    // Méthodes utilitaires pour la gestion des livres
    public void addBook(Book book) {
        if (book != null) {
            books.add(book);
            book.setUser(this);
        }
    }

    public void removeBook(Book book) {
        if (book != null) {
            books.remove(book);
            if (book.getUser() == this) {
                book.setUser(null);
            }
        }
    }

    // Méthodes utilitaires pour la gestion des commandes
    public void addOrder(Order order) {
        if (order != null) {
            orders.add(order);
            order.setUser(this);
        }
    }

    public void removeOrder(Order order) {
        if (order != null) {
            orders.remove(order);
            if (order.getUser() == this) {
                order.setUser(null);
            }
        }
    }

    // Méthodes de validation et initialisation
    @PrePersist
    @PreUpdate
    protected void validateAndInitialize() {
        // Initialiser la date d'enregistrement si elle est nulle (lors de la création)
        if (registrationDate == null) {
            registrationDate = new Date();
        }

        // Valider les données
        if (username != null && (username.length() < 3 || username.length() > 50)) {
            throw new IllegalStateException("La longueur du nom d'utilisateur est invalide");
        }
        if (password != null && password.length() < 8) {
            throw new IllegalStateException("La longueur du mot de passe est invalide");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof User))
            return false;
        User user = (User) o;
        return id != null && id.equals(user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", active=" + active +
                ", numberOfBooks=" + (books != null ? books.size() : 0) +
                ", numberOfOrders=" + (orders != null ? orders.size() : 0) +
                '}';
    }

    // Interface pour la validation lors de la création
    public interface CreateValidation {
    }
}