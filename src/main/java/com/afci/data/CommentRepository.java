package com.afci.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBookId(Long bookId);
    List<Comment> findByUserFirstName(String firstName);
    List<Comment> findByUserLastName(String lastName);
    List<Comment> findByUserFirstNameAndUserLastName(String firstName, String lastName);
	List<Comment> findByUserId(Long userId);


}