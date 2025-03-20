package com.afci.data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "authors")
@PrimaryKeyJoinColumn(name = "user_id")
@DiscriminatorValue("AUTHOR")
public class Author extends User {

    private static final long serialVersionUID = 1L;

    @Column(name = "nationality")
    private String authorNationality;

    @Column(length = 2000)
    private String biography;

    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private Set<Book> authoredBooks = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "authors")
    private Set<Book> books = new HashSet<>();

    @Column(name = "birth_date")
    private LocalDate birthDate;

    // Constructeurs
    public Author() {
    }

    public Author(String username, String password, String email,
            String lastName, String firstName, String authorNationality) {
        super(username, password, email);
        super.setLastName(lastName);
        super.setFirstName(firstName);
        this.authorNationality = authorNationality;
    }

    // Getters et Setters
    public String getAuthorNationality() {
        return authorNationality;
    }

    public void setAuthorNationality(String authorNationality) {
        this.authorNationality = authorNationality;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Set<Book> getAuthoredBooks() {
        return authoredBooks;
    }

    public void setAuthoredBooks(Set<Book> books) {
        this.authoredBooks = books;
        if (books != null) {
            books.forEach(book -> book.setAuthor(this));
        }
    }

    @Override
    public String getFirstName() {
        return super.getFirstName();
    }

    @Override
    public void setFirstName(String firstName) {
        super.setFirstName(firstName);
    }

    @Override
    public String getLastName() {
        return super.getLastName();
    }

    @Override
    public void setLastName(String lastName) {
        super.setLastName(lastName);
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    // Méthodes utilitaires
    public void addAuthoredBook(Book book) {
        if (book != null) {
            authoredBooks.add(book);
            book.setAuthor(this);
        }
    }

    public void removeAuthoredBook(Book book) {
        if (book != null) {
            authoredBooks.remove(book);
            if (book.getAuthor() == this) {
                book.setAuthor(null);
            }
        }
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", authorNationality='" + authorNationality + '\'' +
                ", biography='"
                + (biography != null ? biography.substring(0, Math.min(biography.length(), 50)) + "..." : "null") + '\''
                +
                ", birthDate=" + birthDate +
                ", numberOfAuthoredBooks=" + (authoredBooks != null ? authoredBooks.size() : 0) +
                '}';
    }
}