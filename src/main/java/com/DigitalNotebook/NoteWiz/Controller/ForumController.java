package com.DigitalNotebook.NoteWiz.Controller;


import com.DigitalNotebook.NoteWiz.Model.ForumPost;
import com.DigitalNotebook.NoteWiz.Model.User;
import com.DigitalNotebook.NoteWiz.Service.ForumPostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forum")
public class ForumController {

    @Autowired
    private ForumPostService forumPostService;

    @PostMapping("/create")
    public ResponseEntity<ForumPost> createPost(@RequestBody ForumPost forumPost, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(null);
        }
        ForumPost createdPost = forumPostService.createPost(forumPost,loggedInUser);
        return ResponseEntity.ok(createdPost);
    }
}