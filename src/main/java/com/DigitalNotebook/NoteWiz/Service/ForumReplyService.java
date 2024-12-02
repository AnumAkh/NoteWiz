package com.DigitalNotebook.NoteWiz.Service;

import com.DigitalNotebook.NoteWiz.Model.ForumPost;
import com.DigitalNotebook.NoteWiz.Model.ForumReply;
import com.DigitalNotebook.NoteWiz.Model.User;
import com.DigitalNotebook.NoteWiz.Repository.ForumReplyRepository;
import com.DigitalNotebook.NoteWiz.Repository.ForumPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ForumReplyService {

    @Autowired
    private ForumReplyRepository forumReplyRepository;

    @Autowired
    private ForumPostRepository forumPostRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public ForumReply addReply(int postId, String replyContent, User loggedInUser) {
        // Fetch the ForumPost by ID
        ForumPost post = forumPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Create and populate the reply object
        ForumReply reply = new ForumReply();
        reply.setForumPost(post);
        reply.setUser(loggedInUser);
        reply.setReplyContent(replyContent);
        reply.setCreatedOn(LocalDateTime.now());
        reply.setFormattedCreatedOn(reply.getCreatedOn().format(FORMATTER)); // Format the date

        // Save the reply
        return forumReplyRepository.save(reply);
    }
}