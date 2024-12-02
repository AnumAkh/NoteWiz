package com.DigitalNotebook.NoteWiz.Controller;

import com.DigitalNotebook.NoteWiz.Model.ForumPost;
import com.DigitalNotebook.NoteWiz.Model.Note;
import com.DigitalNotebook.NoteWiz.Model.Notebook;
import com.DigitalNotebook.NoteWiz.Model.User;
import com.DigitalNotebook.NoteWiz.Service.ForumPostService;
import com.DigitalNotebook.NoteWiz.Service.NoteService;
import com.DigitalNotebook.NoteWiz.Service.NotebookService;
import com.DigitalNotebook.NoteWiz.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;
    @Autowired
    private NoteService noteService;
    @Autowired
    private NotebookService notebookService;
    @Autowired
    private ForumPostService forumPostService;

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        List<Note> userNotes = noteService.getUserNotes(loggedInUser.getUserId());

        model.addAttribute("user", loggedInUser);
        model.addAttribute("notes", userNotes);
        model.addAttribute("title", "Home Page");
        return "home";
    }

    @GetMapping("/addNote")
    public String showAddNoteForm() {
        return "addNote";
    }

    @GetMapping("/view-notebooks")
    public String getUserNotebooks(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        int userId = loggedInUser.getUserId();
        List<Notebook> notebooks = notebookService.getUserNotebooksWithNotes(userId);

        model.addAttribute("notebookCount", notebooks.size());
        model.addAttribute("notebooks", notebooks);

        return "notebooks";
    }

    @GetMapping("/forum")
    public String getAllPosts(Model model) {
        List<ForumPost> posts = forumPostService.getAllPostsWithDetails();

        model.addAttribute("posts", posts);

        return "forum"; // Return the forum view
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate(); // Invalidate session
        return "redirect:/login";
    }
}
