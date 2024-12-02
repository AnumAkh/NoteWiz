package com.DigitalNotebook.NoteWiz.Repository;

import com.DigitalNotebook.NoteWiz.Model.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;
import com.DigitalNotebook.NoteWiz.Model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ForumPostRepository extends JpaRepository<ForumPost,Integer> {

    @Query("SELECT p FROM ForumPost p WHERE p.postTitle LIKE %:keyword% OR p.postContent LIKE %:keyword%")
    List<ForumPost> searchPostsByKeyword(@Param("keyword") String keyword);

    @Query("SELECT p, COUNT(r) as commentCount, CONCAT(u.firstName, ' ', u.lastName) as postAuthorName " +
            "FROM ForumPost p " +
            "LEFT JOIN ForumReply r ON r.forumPost = p " +
            "JOIN User u ON p.user = u " +
            "GROUP BY p, u.firstName, u.lastName " +
            "ORDER BY p.createdOn DESC")
    List<Object[]> findAllPostsWithDetails();
}
