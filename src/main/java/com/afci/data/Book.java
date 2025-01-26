package com.afci.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBook;
    private String title;
    private String ISBN;
    private String detail;
    private String picture;
    private String name;
    private Date reward_date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateBook;

    @ManyToOne
    @JoinColumn(name = "authorId")
    private Author author;

    @ManyToMany
    @JoinTable(
        name = "book_category",
        joinColumns = @JoinColumn(name = "idBook"),
        inverseJoinColumns = @JoinColumn(name = "idCategory")
    )
    private Set<Category> categories;

    // Constructors
    public Book() {}

    public Book(Long idBook, String title, String ISBN, String detail, String picture, String name, Date reward_date, Date dateBook, Author author, Set<Category> categories) {
        this.idBook = idBook;
        this.title = title;
        this.ISBN = ISBN;
        this.detail = detail;
        this.picture = picture;
        this.name = name;
        this.reward_date = reward_date;
        this.dateBook = dateBook;
        this.author = author;
        this.categories = categories;
    }

    // Getters and Setters
    public Long getIdBook() {
        return idBook;
    }

    public void setIdBook(Long idBook) {
        this.idBook = idBook;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getReward_date() {
        return reward_date;
    }

    public void setReward_date(Date reward_date) {
        this.reward_date = reward_date;
    }

    public Date getDateBook() {
        return dateBook;
    }

    public void setDateBook(Date dateBook) {
        this.dateBook = dateBook;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Book{" +
                "idBook=" + idBook +
                ", title='" + title + '\'' +
                ", ISBN='" + ISBN + '\'' +
                ", detail='" + detail + '\'' +
                ", picture='" + picture + '\'' +
                ", name='" + name + '\'' +
                ", reward_date=" + reward_date +
                ", dateBook=" + dateBook +
                ", author=" + author +
                ", categories=" + categories +
                '}';
    }
}
