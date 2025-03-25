package com.afci.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date du commentaire est obligatoire")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "comment_date")
    private Date commentDate;

    @NotBlank(message = "Le contenu du commentaire est obligatoire")
    @Column(length = 1000)
    private String content;

    @Column
    private int note;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CommentStatus status = CommentStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Enum pour le statut du commentaire
    public enum CommentStatus {
        PENDING("En attente"),
        REJECTED("Rejeté"),
        APPROVED("Approuvé");

        private final String label;

        CommentStatus(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    // Constructeurs
    public Comment() {
        this.commentDate = new Date();
        this.status = CommentStatus.PENDING;
    }

    public Comment(String content, Customer customer, Book book) {
        this();
        this.content = content;
        this.customer = customer;
        this.book = book;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public CommentStatus getStatus() {
        return status;
    }

    public void setStatus(CommentStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Méthodes métier
    public void updateComment(String newContent) {
        this.content = newContent;
        this.commentDate = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Comment))
            return false;
        Comment comment = (Comment) o;
        return id != null && id.equals(comment.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", commentDate=" + commentDate +
                ", content='"
                + (content != null ? content.substring(0, Math.min(content.length(), 50)) + "..." : "null") + '\'' +
                ", status='" + status + '\'' +
                ", customer=" + (customer != null ? customer.getUsername() : "null") +
                ", book=" + (book != null ? book.getTitle() : "null") +
                '}';
    }
}