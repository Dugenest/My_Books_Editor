package com.afci.data;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;

    // Attributs
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_comment;
    private Date comment_date;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // Constructeur
    public Comment() {
    }

    public Comment(Long id_comment, Date comment_date, String comment) {
        this.id_comment = id_comment;
        this.comment_date = comment_date;
        this.comment = comment;
    }

    // Getters et Setters
    public Long getId_comment() {
        return id_comment;
    }

    public void setId_comment(Long id_comment) {
        this.id_comment = id_comment;
    }

    public Date getComment_date() {
        return comment_date;
    }

    public void setComment_date(Date comment_date) {
        this.comment_date = comment_date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // Méthodes
    public void addComment(String commentData) {
        this.comment = commentData;
        this.comment_date = new Date(); // Set the current date
    }

    public void updateComment(Long commentId, String newCommentData) {
        if (this.id_comment == commentId) {
            this.comment = newCommentData;
            this.comment_date = new Date(); // Update the date to current date
        }
    }

    public void deleteComment(Long commentId) {
        if (this.id_comment == commentId) {
            this.comment = null;
            this.comment_date = null;
        }
    }

    public String getComment(Long commentId) {
        if (this.id_comment == commentId) {
            return this.comment;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id_comment=" + id_comment +
                ", comment_date=" + comment_date +
                ", comment='" + comment + '\'' +
                ", customer=" + (customer != null ? customer.getIdCustomer() : "null") +
                '}';
    }
}
