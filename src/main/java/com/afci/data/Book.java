package com.afci.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Column(nullable = false)
    private String title;

    @Column(unique = true)
    @Pattern(regexp = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ])?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$", message = "Format ISBN invalide")
    private String ISBN;

    @Column(length = 2000)
    private String detail;

    private String picture;

    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.0", message = "Le prix doit être positif")
    private Double price;

    @Min(value = 0, message = "Le stock ne peut pas être négatif")
    private Integer stock;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "reward_date")
    @Temporal(TemporalType.DATE)
    private Date rewardDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "publish_date")
    @Temporal(TemporalType.DATE)
    private LocalDate publishDate;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne
    @JoinColumn(name = "editor_id")
    private Editor editor;

    @ManyToOne
    @JoinColumn(name = "administrator_id")
    private Administrator administrator;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(name = "book_category", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    @ManyToMany(mappedBy = "books")
    private Set<Order> orders = new HashSet<>();

    @ManyToMany(mappedBy = "books")
    private Set<Basket> baskets = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "category_id") // Adjust column name if needed
    private Category category;

    // Constructeurs
    public Book() {
        this.stock = 0;
        this.price = 0.0;
    }

    public Book(String title, String ISBN, String detail, double price) {
        this.title = title;
        this.ISBN = ISBN;
        this.detail = detail;
        this.price = price;
        this.stock = 0; // Valeur par défaut
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

    public Date getRewardDate() {
        return rewardDate;
    }

    public void setRewardDate(Date rewardDate) {
        this.rewardDate = rewardDate;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
        if (author != null && !author.getAuthoredBooks().contains(this)) {
            author.getAuthoredBooks().add(this);
        }
    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
        if (editor != null && !editor.getBooks().contains(this)) {
            editor.getBooks().add(this);
        }
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
        if (administrator != null && !administrator.getManagedBooks().contains(this)) {
            administrator.getManagedBooks().add(this);
        }
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Set<Basket> getBaskets() {
        return baskets;
    }

    public void setBaskets(Set<Basket> baskets) {
        this.baskets = baskets;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Méthodes métier
    public boolean isInStock() {
        return stock != null && stock > 0;
    }

    public boolean decreaseStock(int quantity) {
        if (stock >= quantity) {
            stock -= quantity;
            return true;
        }
        return false;
    }

    public void increaseStock(int quantity) {
        if (stock == null)
            stock = 0;
        stock += quantity;
    }

    // Méthodes utilitaires
    public void addCategory(Category category) {
        if (category != null) {
            categories.add(category);
            category.getBooks().add(this);
        }
    }

    public void removeCategory(Category category) {
        if (category != null) {
            categories.remove(category);
            category.getBooks().remove(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Book))
            return false;
        Book book = (Book) o;
        return id != null && id.equals(book.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", ISBN='" + ISBN + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", categories=" + categories.size() +
                '}';
    }
}