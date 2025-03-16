package com.afci.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "editors")
public class Editor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id")
    private Long companyId;

    @OneToMany(mappedBy = "editor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Book> books = new HashSet<>();

    // Constructeurs
    public Editor() {}

    public Editor(Long companyId) {
        this.companyId = companyId;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Set<Book> getBooks() { return books; }
    public void setBooks(Set<Book> books) {
        this.books = books;
        if (books != null) {
            books.forEach(book -> book.setEditor(this));
        }
    }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

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
        if (this == o) return true;
        if (!(o instanceof Editor)) return false;
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
               ", companyId=" + companyId +
               ", books=" + (books != null ? books.size() : 0) +
               '}';
    }
}
