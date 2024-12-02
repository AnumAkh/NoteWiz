package com.DigitalNotebook.NoteWiz.Controller;


import com.DigitalNotebook.NoteWiz.Model.ForumPost;
import com.DigitalNotebook.NoteWiz.Model.ForumReply;
import com.DigitalNotebook.NoteWiz.Model.User;
import com.DigitalNotebook.NoteWiz.Repository.ForumPostRepository;
import com.DigitalNotebook.NoteWiz.Repository.ForumReplyRepository;
import com.DigitalNotebook.NoteWiz.Repository.UserRepository;
import com.DigitalNotebook.NoteWiz.Service.ForumPostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private ForumPostService forumPostService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ForumReplyRepository forumReplyRepository;


    @PostMapping("/create")
    public ResponseEntity<ForumPost> createPost(
            @RequestBody ForumPost forumPost,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            throw new RuntimeException("User not logged in");
        }

        ForumPost createdPost = forumPostService.createPost(forumPost, loggedInUser);
        return ResponseEntity.ok(createdPost);
    }


//    @GetMapping("/search")
//    public ResponseEntity<List<ForumPost>> searchPosts(@RequestParam("keyword") String keyword) {
//        List<ForumPost> posts = forumPostService.searchPosts(keyword);
//
//        for (ForumPost post : posts) {
//            if (post.getCreatedOn() != null) {
//                post.setFormattedCreatedOn(post.getCreatedOn().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
//            }
//        }
//        return ResponseEntity.ok(posts);
//    }


    @GetMapping("/search")
    public String searchPosts(@RequestParam("keyword") String keyword, Model model) {
        List<ForumPost> posts = forumPostService.searchPosts(keyword);

        for (ForumPost post : posts) {
            if (post.getCreatedOn() != null) {
                post.setFormattedCreatedOn(post.getCreatedOn().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
            }
        }

        model.addAttribute("posts", posts);
        return "forum"; // Render the forum.html page with filtered posts
    }


    @GetMapping("/{postId}")
    public ResponseEntity<ForumPost> getPostById(@PathVariable("postId") int postId) {
        ForumPost forumPost = forumPostService.getPostById(postId);
        if (forumPost == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(forumPost);
    }

    @PostMapping("/{action}")
    public ResponseEntity<Map<String, Integer>> handleVote(@RequestBody Map<String, Integer> payload,
                                                           @PathVariable("action") String action,
                                                           HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        int postId = payload.get("postId");
        boolean isUpvote = "upvote".equalsIgnoreCase(action);

        // Update the database via the service layer
        Map<String, Integer> updatedCounts = forumPostService.handleVote(postId, isUpvote, loggedInUser);
        return ResponseEntity.ok(updatedCounts);
    }

    @GetMapping("/post/{postId}")
    public String getPostDetails(@PathVariable("postId") int postId, Model model) {
        ForumPost post = forumPostService.getPostById(postId);

        if (post == null) {
            return "error"; // Return an error page if the post is not found
        }

        // Fetch replies for the post
        List<ForumReply> replies = forumReplyRepository.findByForumPost(post);

        model.addAttribute("post", post);
        model.addAttribute("replies", replies);
        return "forumPost"; // Return the forumPost.html template
    }

}