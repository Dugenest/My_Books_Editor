package com.afci.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afci.data.Comment;
import com.afci.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
@Tag(name = "Comment Management", description = "Comment operations")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Operation(summary = "Get comments for book")
    @GetMapping("/book/{bookId}")
    public ResponseEntity<Object> getBookComments(
        @Parameter(description = "Book ID") @PathVariable Long bookId
    ) {
        return ResponseEntity.ok(commentService.getBookComments(bookId));
    }

    @Operation(summary = "Add comment")
    @PostMapping("/book/{bookId}")
    public ResponseEntity<Comment> addComment(
        @Parameter(description = "Book ID") @PathVariable Long bookId,
        @Parameter(description = "Comment details") @Valid @RequestBody Comment comment
    ) {
        return new ResponseEntity<>(commentService.addComment(bookId, comment), HttpStatus.CREATED);
    }


    @Operation(summary = "Update comment")
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(
        @Parameter(description = "Comment ID") @PathVariable Long id,
        @Parameter(description = "Comment details") @Valid @RequestBody Comment comment
    ) {
        return ResponseEntity.ok(commentService.updateComment(comment));
    }

    @Operation(summary = "Delete comment")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
        @Parameter(description = "Comment ID") @PathVariable Long id
    ) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}