package com.afci.data;

import java.io.Serializable;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Serial implements Serializable {
    private static final long serialVersionUID = 1L;

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_serial;
    private String name;

    @OneToMany(mappedBy = "serial", cascade = CascadeType.ALL)
    private Set<Book> books;

    @OneToMany(mappedBy = "serial", cascade = CascadeType.ALL)
    private Set<Author> authors;

    // Constructeur
    public Serial() {
    }

    public Serial(Long id_serial, String name) {
        this.id_serial = id_serial;
        this.name = name;
    }

    // Getters et Setters
    public Long getId_serial() {
        return id_serial;
    }

    public void setId_serial(Long id_serial) {
        this.id_serial = id_serial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    @Override
    public String toString() {
        return "Serial{" +
                "id_serial=" + id_serial +
                ", name='" + name + '\'' +
                ", books=" + books +
                ", authors=" + authors +
                '}';
    }
}