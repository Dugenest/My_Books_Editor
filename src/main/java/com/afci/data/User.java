package com.afci.data;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String password;
    private int role;

    // OneToMany relation with Book
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Book> books;

    // Constructors
    public User() {
    }

    public User(Long userId, String username, String password, int role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    // Getters and Setters
    public void setUserId(Long userId) {
        this.userId = userId;
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

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    // Méthodes personnalisées
    public boolean isAdmin() {
        return this.role == 1;
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public static User createProfile(String username, String password, int role) {
        return new User();
    }

    // Méthodes equals et hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return role == user.role &&
                Objects.equals(userId, user.userId) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, role);
    }

    // Méthodes toString
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}