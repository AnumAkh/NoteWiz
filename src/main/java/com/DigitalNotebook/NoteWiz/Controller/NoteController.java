package com.DigitalNotebook.NoteWiz.Controller;

import com.DigitalNotebook.NoteWiz.Model.Note;
import com.DigitalNotebook.NoteWiz.Model.Tag;
import com.DigitalNotebook.NoteWiz.Model.User;
import com.DigitalNotebook.NoteWiz.Service.NoteService;
import com.DigitalNotebook.NoteWiz.Service.TagService;
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
import java.util.Map;


@Controller
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

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

    @GetMapping("/viewNote")
    public String viewNote(@RequestParam int noteId, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to login if user is not logged in
        }

        Note note = noteService.findNoteById(noteId);

        if (note == null || note.getUser().getUserId() != loggedInUser.getUserId()) {
            return "redirect:/home"; // Redirect if the note doesn't exist or doesn't belong to the user
        }

        model.addAttribute("noteId", note.getNoteId());
        model.addAttribute("noteTitle", note.getNoteTitle());
        model.addAttribute("noteContent", note.getContent());
        return "viewNote"; // Thymeleaf template to display the note
    }

    @PostMapping("/addTag")
    public ResponseEntity<?> addTag(@RequestBody Map<String, String> tagData, HttpSession session) {
        System.out.println("Tag Data: " + tagData);
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String tagName = tagData.get("tagName");
        int noteId;
        try {
            noteId = Integer.parseInt(tagData.get("noteId"));
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Note ID");
        }

        // Fetch the Note object from the service layer
        Note note = noteService.findNoteById(noteId);
        if (note == null || note.getUser().getUserId() != loggedInUser.getUserId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access to note");
        }

        // Create a new Tag object
        Tag tag = new Tag();
        tag.setTagName(tagName);

        // Set the Note and User entities instead of noteId and userId
        tag.setNote(note); // Set the associated Note object
        tag.setUser(loggedInUser); // Set the associated User object

        // Save the Tag through the TagService
        tagService.saveTag(tag);

        return ResponseEntity.ok("Tag added successfully!");
    }

    @PostMapping("/share")
    public ResponseEntity<?> shareNote(@RequestParam(value = "noteId", required = true) int noteId,
                                       @RequestParam(value = "collaboratorEmail", required = true) String collaboratorEmail,
                                       HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        User collaborator = userService.getUserByEmail(collaboratorEmail);
        if (collaborator == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Collaborator not found");
        }
        System.out.println("Note ID: " + noteId);
        System.out.println("Collaborator Email: " + collaboratorEmail);
        try {
            noteService.shareNoteWithCollaborator(noteId, collaborator.getUserId());
            return ResponseEntity.ok("Note shared successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sharing note: " + e.getMessage());
        }
    }
}

