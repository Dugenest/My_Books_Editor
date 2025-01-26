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
public class Editor implements Serializable {
    private static final long serialVersionUID = 1L;

    // Attributs
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_editor;
    private String name;
    private String address;

    @OneToMany(mappedBy = "editor", cascade = CascadeType.ALL)
    private Set<Book> books;

    // Constructeur
    public Editor(Long id_editor, String name, String address) {
        this.id_editor = id_editor;
        this.name = name;
        this.address = address;
    }

    // Getters et Setters
    public Long getId_editor() {
        return id_editor;
    }

    public void setId_editor(Long id_editor) {
        this.id_editor = id_editor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    // Méthodes
    public void addEditor(String editorData) {
        // Logic to add an editor
        // Example: parse editorData and set the fields
        String[] data = editorData.split(",");
        this.id_editor = Long.valueOf(data[0]);
        this.name = data[1];
        this.address = data[2];
    }

    public String getEditorDetails(Long editorId) {
        // Logic to get editor details
        // Example: return details if id matches
        if (this.id_editor.equals(editorId)) {
            return this.toString();
        }
        return "Editor not found";
    }

    public void updateEditor(String editorData) {
        // Logic to update an editor
        // Example: parse editorData and update the fields
        String[] data = editorData.split(",");
        if (this.id_editor.equals(Long.valueOf(data[0]))) {
            this.name = data[1];
            this.address = data[2];
        }
    }

    public void deleteEditor(Long editorId) {
        // Logic to delete an editor
        // Example: clear fields if id matches
        if (this.id_editor.equals(editorId)) {
            this.id_editor = null;
            this.name = null;
            this.address = null;
        }
    }

    @Override
    public String toString() {
        return "Editor [id_editor=" + id_editor + ", name=" + name + ", address=" + address + "]";
    }
}
