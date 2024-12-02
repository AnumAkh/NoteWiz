package com.DigitalNotebook.NoteWiz.Controller;

import com.DigitalNotebook.NoteWiz.Model.Note;
import com.DigitalNotebook.NoteWiz.Model.Tag;
import com.DigitalNotebook.NoteWiz.Model.User;
import com.DigitalNotebook.NoteWiz.Service.NoteService;
import com.DigitalNotebook.NoteWiz.Service.TagService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

//@Controller
//@RequestMapping("/tags")
//public class TagController {
//
//    @Autowired
//    private TagService tagService;
//
//    @GetMapping("/by-user")
//    @ResponseBody
//    public List<Tag> getTagsByUser(HttpSession session) {
//        User loggedInUser = (User) session.getAttribute("loggedInUser");
//
//        if (loggedInUser == null) {
//            return Collections.emptyList(); // If not logged in, return an empty list
//        }
//
//        return tagService.getTagsByUser(loggedInUser.getUserId());
//    }
//}

@Controller
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private NoteService noteService;

    @GetMapping("/view-tags")
    public String viewTags(HttpSession session, Model model) {
        // Retrieve the currently logged-in user
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to login if not logged in
        }

        // Fetch all tags for the user
        List<Tag> tags = tagService.getTagsByUser(loggedInUser.getUserId());

        // Fetch the notes for the logged-in user
        List<Note> notes = noteService.getUserNotes(loggedInUser.getUserId());

        // Add the tags and notes to the model
        model.addAttribute("tags", tags);
        model.addAttribute("notes", notes);

        return "viewTags"; // Return to the Thymeleaf template to display tags and notes
    }
}

