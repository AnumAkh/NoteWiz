package com.DigitalNotebook.NoteWiz.Controller;

import com.DigitalNotebook.NoteWiz.Model.Note;
import com.DigitalNotebook.NoteWiz.Model.User;
import com.DigitalNotebook.NoteWiz.Service.NoteService;
import com.DigitalNotebook.NoteWiz.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@Controller
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

    // Delete a note by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable int id, HttpSession session) {
        // Retrieve user session and validate if needed
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean isDeleted = noteService.deleteNoteByIdAndUser(id, currentUser);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //edit note
    @GetMapping("/editNote")
    public String getNoteForEdit(@RequestParam int noteId, HttpSession session, Model model) {
        // Example: Check if user is logged in
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to login page if user is not logged in
        }

        // Fetch the note based on noteId
        if (noteId != 0) {
            Note note = noteService.findNoteById(noteId);
            if (note != null) {
                model.addAttribute("noteTitle", note.getNoteTitle());
                model.addAttribute("noteContent", note.getContent());
            }
        }

        return "editNote"; // Name of the Thymeleaf template
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateNote(@RequestBody Note note, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Unauthorized if no user is logged in
        }

        // Ensure the note belongs to the logged-in user
        Note existingNote = noteService.findNoteById(note.getNoteId());
        if (existingNote == null || !(existingNote.getUser().getUserId() == loggedInUser.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Update the existing note
        existingNote.setNoteTitle(note.getNoteTitle());
        existingNote.setContent(note.getContent());
        existingNote.setUpdatedAt(LocalDateTime.now());

        Note updatedNote = noteService.saveNote(existingNote);
        return ResponseEntity.ok(updatedNote);
    }

}

