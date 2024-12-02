package com.DigitalNotebook.NoteWiz.Controller;

import com.DigitalNotebook.NoteWiz.Model.ForumPost;
import com.DigitalNotebook.NoteWiz.Model.ForumReply;
import com.DigitalNotebook.NoteWiz.Model.User;
import com.DigitalNotebook.NoteWiz.Repository.ForumPostRepository;
import com.DigitalNotebook.NoteWiz.Repository.ForumReplyRepository;
import com.DigitalNotebook.NoteWiz.Service.ForumPostService;
import com.DigitalNotebook.NoteWiz.Service.ForumReplyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/forumPost/post/{postId}")
public class ForumPostController {

    @Autowired
    private ForumPostService forumPostService;

    @Autowired
    private ForumReplyRepository forumReplyRepository;

    @Autowired
    private ForumReplyService forumReplyService;

    @PostMapping("/reply")
    public ResponseEntity<ForumReply> addReply(@PathVariable("postId") int postId,
                                               @RequestBody Map<String, String> payload,
                                               HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        String replyContent = payload.get("replyContent");
        if (replyContent == null || replyContent.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        ForumReply savedReply = forumReplyService.addReply(postId, replyContent, loggedInUser);

        return ResponseEntity.ok(savedReply);
    }

}
