package com.DigitalNotebook.NoteWiz.Repository;

import com.DigitalNotebook.NoteWiz.Model.ForumPost;
import com.DigitalNotebook.NoteWiz.Model.UpvoteDownvote;
import com.DigitalNotebook.NoteWiz.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpvoteDownvoteRepository extends JpaRepository<UpvoteDownvote,Integer> {

    UpvoteDownvote findByUserAndForumPost(User user, ForumPost forumPost);
}
