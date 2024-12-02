package com.DigitalNotebook.NoteWiz.Controller;

import com.DigitalNotebook.NoteWiz.Model.Note;
import com.DigitalNotebook.NoteWiz.Model.User;
import com.DigitalNotebook.NoteWiz.Service.NoteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ShareNoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping("/sharedNotes")
    public String getSharedNotes(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Fetch shared notes
        List<Note> sharedNotes = noteService.getSharedNotes(loggedInUser.getUserId());

        model.addAttribute("user", loggedInUser);
        model.addAttribute("notes", sharedNotes);
        model.addAttribute("title", "Shared Notes");
        return "shareNote"; // Render shareNote.html
    }
    @GetMapping("/viewSharedNote")
    public String viewNote(@RequestParam int noteId, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Note note = noteService.findNoteById(noteId);

        if (note == null ) {
            return "redirect:/home";
        }

        model.addAttribute("noteId", note.getNoteId());
        model.addAttribute("noteTitle", note.getNoteTitle());
        model.addAttribute("noteContent", note.getContent());
        return "viewNote"; // Thymeleaf template to display the note
    }
}