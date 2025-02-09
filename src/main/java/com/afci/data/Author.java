package com.afci.data;

import jakarta.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authors")
@PrimaryKeyJoinColumn(name = "user_id")
@DiscriminatorValue("AUTHOR")
public class Author extends User {

    private static final long serialVersionUID = 1L;

    @Column(name = "author_lastname")
    private String authorLastname;

    @Column(name = "author_firstname")
    private String authorFirstname;
    
    @Column(name = "nationality")
    private String authorNationality;

    @Column(length = 2000)
    private String biography;

    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private Set<Book> authoredBooks = new HashSet<>();

    // Constructeurs
    public Author() {}

    public Author(String username, String password, String email,
                 String authorLastname, String authorFirstname, String authorNationality) {
        super(username, password, email);
        this.authorLastname = authorLastname;
        this.authorFirstname = authorFirstname;
        this.authorNationality = authorNationality;
    }

    // Getters et Setters
    public String getAuthorLastname() { return authorLastname; }
    public void setAuthorLastname(String authorLastname) { this.authorLastname = authorLastname; }

    public String getAuthorFirstname() { return authorFirstname; }
    public void setAuthorFirstname(String authorFirstname) { 
        this.authorFirstname = authorFirstname; 
    }

    public String getAuthorNationality() { return authorNationality; }
    public void setAuthorNationality(String authorNationality) { 
        this.authorNationality = authorNationality; 
    }

    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }

    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }

    public Set<Book> getAuthoredBooks() { return authoredBooks; }
    public void setAuthoredBooks(Set<Book> books) {
        this.authoredBooks = books;
        if (books != null) {
            books.forEach(book -> book.setAuthor(this));
        }
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
               ", authorLastname='" + authorLastname + '\'' +
               ", authorFirstname='" + authorFirstname + '\'' +
               ", authorNationality='" + authorNationality + '\'' +
               ", biography='" + (biography != null ? biography.substring(0, Math.min(biography.length(), 50)) + "..." : "null") + '\'' +
               ", birthDate=" + birthDate +
               ", numberOfAuthoredBooks=" + (authoredBooks != null ? authoredBooks.size() : 0) +
               '}';
    }
}