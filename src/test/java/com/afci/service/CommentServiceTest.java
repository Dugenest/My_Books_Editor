package com.afci.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.afci.data.Book;
import com.afci.data.Comment;
import com.afci.data.CommentRepository;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

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
    void getAllComments_ShouldReturnAllComments() {
        List<Comment> comments = Arrays.asList(comment);
        when(commentRepository.findAll()).thenReturn(comments);

        List<Comment> result = commentService.getAllComments();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(commentRepository).findAll();
    }

    @Test
    void getCommentById_ShouldReturnComment() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        Optional<Comment> result = commentService.getCommentById(1L);

        assertTrue(result.isPresent());
        assertEquals(comment.getContent(), result.get().getContent());
    }

    @Test
    void createComment_ShouldReturnSavedComment() {
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment result = commentService.createComment(comment);

        assertNotNull(result);
        assertEquals(comment.getContent(), result.getContent());
    }

    @Test
    void updateComment_WhenExists_ShouldReturnUpdatedComment() {
        when(commentRepository.existsById(1L)).thenReturn(true);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment result = commentService.updateComment(comment);

        assertNotNull(result);
        assertEquals(comment.getContent(), result.getContent());
    }

    @Test
    void updateComment_WhenNotExists_ShouldThrowException() {
        when(commentRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> commentService.updateComment(comment));
    }

    @Test
    void deleteComment_ShouldCallRepositoryDelete() {
        doNothing().when(commentRepository).deleteById(1L);

        commentService.deleteComment(1L);

        verify(commentRepository).deleteById(1L);
    }

    @Test
    void findByBookId_ShouldReturnComments() {
        List<Comment> comments = Arrays.asList(comment);
        when(commentRepository.findByBookId(1L)).thenReturn(comments);

        List<Comment> result = commentService.findByBookId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getBookComments_ShouldReturnComments() {
        List<Comment> comments = Arrays.asList(comment);
        when(commentRepository.findByBookId(1L)).thenReturn(comments);

        List<Comment> result = commentService.getBookComments(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void addComment_ShouldReturnSavedComment() {
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment result = commentService.addComment(1L, comment);

        assertNotNull(result);
        assertEquals(1L, result.getBook().getId());
    }
} 