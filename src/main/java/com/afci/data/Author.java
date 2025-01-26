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
public class Author extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorId;
    private String authorName;
    private String authorFirstname;
    private String authorNationality;

    // OneToMany relation with Book
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private Set<Book> books;

    // Constructors, Getters, Setters, toString
    public Author() {
    }

    public Author(String authorName, String authorFirstname, String authorNationality) {
        this.authorName = authorName;
        this.authorFirstname = authorFirstname;
        this.authorNationality = authorNationality;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorFirstname() {
        return authorFirstname;
    }

    public void setAuthorFirstname(String authorFirstname) {
        this.authorFirstname = authorFirstname;
    }

    public String getAuthorNationality() {
        return authorNationality;
    }

    public void setAuthorNationality(String authorNationality) {
        this.authorNationality = authorNationality;
    }

    @Override
    public String toString() {
        return "Author [authorId=" + authorId + ", authorName=" + authorName + ", authorFirstname=" + authorFirstname
                + ", authorNationality=" + authorNationality + "]";
    }
}
