package com.DigitalNotebook.NoteWiz.Controller;

import com.DigitalNotebook.NoteWiz.Model.Note;
import com.DigitalNotebook.NoteWiz.Model.User;
import com.DigitalNotebook.NoteWiz.Service.NoteService;
import com.DigitalNotebook.NoteWiz.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<Note> addNote(@RequestBody Note note, HttpSession session) {
        // Retrieve the currently logged-in user's details
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(null); // Unauthorized if no user is logged in
        }

        // Assign the user to the note
        note.setUser(loggedInUser);
        note.setCreatedAt(LocalDateTime.now());

        // Save the note
        Note savedNote = noteService.saveNote(note);
        return ResponseEntity.ok(savedNote);
    }

    // Get all notes of a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Note>> getUserNotes(@PathVariable int userId) {
        List<Note> notes = noteService.getUserNotes(userId);
        return ResponseEntity.ok(notes);
    }
}
