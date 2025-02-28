package com.afci.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.afci.data.Book;
import com.afci.data.Comment;
import com.afci.service.CommentService;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private Comment comment;
    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        
        comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test comment");
        comment.setBook(book);
    }

    @Test
    void getBookComments_ShouldReturnComments() {
        List<Comment> comments = Arrays.asList(comment);
        when(commentService.getBookComments(1L)).thenReturn(comments);

        ResponseEntity<Object> response = commentController.getBookComments(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void addComment_ShouldReturnCreatedComment() {
        when(commentService.addComment(eq(1L), any(Comment.class))).thenReturn(comment);

        ResponseEntity<Comment> response = commentController.addComment(1L, comment);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(comment.getContent(), response.getBody().getContent());
    }

    @Test
    void updateComment_ShouldReturnUpdatedComment() {
        when(commentService.updateComment(any(Comment.class))).thenReturn(comment);

        ResponseEntity<Comment> response = commentController.updateComment(1L, comment);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(comment.getContent(), response.getBody().getContent());
    }

    @Test
    void deleteComment_ShouldReturnNoContent() {
        doNothing().when(commentService).deleteComment(1L);

        ResponseEntity<Void> response = commentController.deleteComment(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
} 