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

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false)
    private String name;

    private String address;
    private String phone;
    private String email;
    private String companyName;

    @OneToMany(mappedBy = "editor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Book> books = new HashSet<>();

    // Constructeurs
    public Editor() {}

    public Editor(String name, String address, String phone, String email, String companyName) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.companyName = companyName;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public Set<Book> getBooks() { return books; }
    public void setBooks(Set<Book> books) {
        this.books = books;
        if (books != null) {
            books.forEach(book -> book.setEditor(this));
        }
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
               ", name='" + name + '\'' +
               ", email='" + email + '\'' +
               ", books=" + (books != null ? books.size() : 0) +
               '}';
    }
}
