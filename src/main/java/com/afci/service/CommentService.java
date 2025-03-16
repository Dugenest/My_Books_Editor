package com.afci.service;

import com.afci.data.Book;
import com.afci.data.Comment;
import com.afci.repository.CommentRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment updateComment(Comment comment) {
        if (commentRepository.existsById(comment.getId())) {
            return commentRepository.save(comment);
        }
        throw new RuntimeException("Commentaire non trouvé avec l'ID : " + comment.getId());
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public List<Comment> findByBookId(Long bookId) {
        return commentRepository.findByBookId(bookId);
    }

    public List<Comment> findCommentsByUserId(Long userId, String searchTerm) {
        // Vous pouvez chercher en fonction du prénom ou du nom
        if (searchTerm != null && !searchTerm.isEmpty()) {
            List<Comment> commentsByFirstName = commentRepository.findByUserFirstName(searchTerm);
            List<Comment> commentsByLastName = commentRepository.findByUserLastName(searchTerm);

            // Fusionner les résultats (ou choisir d'une autre manière de combiner les deux)
            commentsByFirstName.addAll(commentsByLastName);
            return commentsByFirstName;
        }
        // Si le terme de recherche est vide ou null, on cherche par userId
        return commentRepository.findByUserId(userId);
    }


    public List<Comment> getBookComments(Long bookId) {
        return commentRepository.findByBookId(bookId);
    }

    public Comment addComment(Long bookId, @Valid Comment comment) {
        Book book = new Book();  // Vérifiez que Book est bien défini
        book.setId(bookId);
        comment.setBook(book);
        return commentRepository.save(comment);
    }

    
    
}