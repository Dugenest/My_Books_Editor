package com.afci.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class BookDTO {
    private Long id;
    private String title;
    private String isbn;
    private String detail;
    private Double price;
    private Integer stock;
    private String picture;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate rewardDate;

    private EditorDTO editor;
    private Set<CategoryDTO> categories = new HashSet<>();
    private Set<AuthorDTO> authors = new HashSet<>();

    // Constructeurs
    public BookDTO() {
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public LocalDate getRewardDate() {
        return rewardDate;
    }

    public void setRewardDate(LocalDate rewardDate) {
        this.rewardDate = rewardDate;
    }

    public EditorDTO getEditor() {
        return editor;
    }

    public void setEditor(EditorDTO editor) {
        this.editor = editor;
    }

    public Set<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryDTO> categories) {
        this.categories = categories;
    }

    public Set<AuthorDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorDTO> authors) {
        this.authors = authors;
    }
}
