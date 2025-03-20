package com.afci.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "editors")
public class Editor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company")
    private String company;

    @JsonIgnore
    @OneToMany(mappedBy = "editor", cascade = CascadeType.ALL)
    private Set<Book> books = new HashSet<>();

    // Constructeurs
    public Editor() {
    }

    public Editor(String company) {
        this.company = company;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
        if (books != null) {
            books.forEach(book -> book.setEditor(this));
        }
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    // Méthodes utilitaires
    public void addBook(Book book) {
        if (book != null) {
            books.add(book);
            book.setEditor(this);
        }
    }

    public void removeBook(Book book) {
        if (book != null) {
            books.remove(book);
            if (book.getEditor() == this) {
                book.setEditor(null);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Editor))
            return false;
        Editor editor = (Editor) o;
        return id != null && id.equals(editor.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Editor{" +
                "id=" + id +
                ", company=" + company +
                ", books=" + (books != null ? books.size() : 0) +
                '}';
    }
}
