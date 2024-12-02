package com.DigitalNotebook.NoteWiz.Service;

import com.DigitalNotebook.NoteWiz.Model.*;
import com.DigitalNotebook.NoteWiz.Repository.ForumPostRepository;
import com.DigitalNotebook.NoteWiz.Repository.ForumReplyRepository;
import com.DigitalNotebook.NoteWiz.Repository.UpvoteDownvoteRepository;
import com.DigitalNotebook.NoteWiz.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ForumPostService {

    @Autowired
    private ForumPostRepository forumPostRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ForumReplyRepository forumReplyRepository;


    @Transactional
    public ForumPost createPost(ForumPost forumPost,User user) {
        System.out.println(user.getUserId());
        forumPost.setUser(user);
        forumPost.setCreatedOn(LocalDateTime.now());
        forumPost.setTotalUpvote(0);
        forumPost.setTotalDownvote(0);

        return forumPostRepository.save(forumPost);
    }



    public List<ForumPost> getAllPostsWithDetails() {
        List<Object[]> results = forumPostRepository.findAllPostsWithDetails();
        List<ForumPost> enrichedPosts = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

        for (Object[] result : results) {
            ForumPost post = (ForumPost) result[0];
            Long commentCount = (Long) result[1];
            String postAuthorName = (String) result[2];

            post.setCommentCount(commentCount.intValue());
            post.setPostAuthorName(postAuthorName);

            // Format the createdOn field
            if (post.getCreatedOn() != null) {
                post.setFormattedCreatedOn(post.getCreatedOn().format(formatter));
            }

            enrichedPosts.add(post);
        }
        return enrichedPosts;
    }

    public ForumPost getPostById(int postId) {
        return forumPostRepository.findById(postId).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<ForumPost> searchPosts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be null or empty");
        }
        return forumPostRepository.searchPostsByKeyword(keyword);
    }

    @Autowired
    private UpvoteDownvoteRepository upvoteDownvoteRepository;

    @Transactional
    public Map<String, Integer> handleVote(int postId, boolean isUpvote, User user) {
        ForumPost post = forumPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Check if the user has already voted
        UpvoteDownvote existingVote = upvoteDownvoteRepository.findByUserAndForumPost(user, post);

        if (existingVote != null) {
            // If the user is changing their vote
            if (existingVote.getVoteType() == (isUpvote ? VoteType.UPVOTE : VoteType.DOWNVOTE)) {
                throw new RuntimeException("You have already " + (isUpvote ? "upvoted" : "downvoted") + " this post");
            }
            // Update the vote type
            existingVote.setVoteType(isUpvote ? VoteType.UPVOTE : VoteType.DOWNVOTE);
        } else {
            // Create a new vote record
            UpvoteDownvote newVote = new UpvoteDownvote();
            newVote.setForumPost(post);
            newVote.setUser(user);
            newVote.setVoteType(isUpvote ? VoteType.UPVOTE : VoteType.DOWNVOTE);
            upvoteDownvoteRepository.save(newVote);
        }

        // Update the post's upvote/downvote count
        if (isUpvote) {
            post.setTotalUpvote(post.getTotalUpvote() + 1);
        } else {
            post.setTotalDownvote(post.getTotalDownvote() + 1);
        }
        forumPostRepository.save(post);

        // Return the updated counts
        Map<String, Integer> updatedCounts = new HashMap<>();
        updatedCounts.put("totalUpvote", post.getTotalUpvote());
        updatedCounts.put("totalDownvote", post.getTotalDownvote());
        return updatedCounts;
    }

    public ForumPost getPostWithReplies(int postId) {
        ForumPost post = forumPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        List<ForumReply> replies = forumReplyRepository.findByForumPost(post);
        post.setReplies(replies); // Set replies to the transient field
        return post;
    }

}
