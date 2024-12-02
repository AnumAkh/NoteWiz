package com.DigitalNotebook.NoteWiz.Controller;

import com.DigitalNotebook.NoteWiz.Dto.NoteNotebook;
import com.DigitalNotebook.NoteWiz.Service.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.DigitalNotebook.NoteWiz.Model.Notebook;
import com.DigitalNotebook.NoteWiz.Model.User;
import com.DigitalNotebook.NoteWiz.Service.NotebookService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notebooks")
public class NotebookController {

    @Autowired
    private NotebookService notebookService;

    @Autowired
    private NoteService noteService;

    @GetMapping("/all")
    public ResponseEntity<List<Notebook>> getAllNotebooks() {
        List<Notebook> notebooks = notebookService.getAllNotebooks();
        return ResponseEntity.ok(notebooks);
    }

    @PostMapping("/create")
    public ResponseEntity<Notebook> createNotebook(@RequestBody Notebook notebook, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(null);
        }

        notebook.setUser(loggedInUser);
        notebook.setCreatedOn(LocalDateTime.now());

        Notebook savedNotebook = notebookService.createNotebook(notebook);
        return new ResponseEntity<>(savedNotebook, HttpStatus.CREATED);
    }

    @PostMapping("/addToNotebook")
    public ResponseEntity<Map<String, Object>> addNoteToNotebook(@RequestBody NoteNotebook request) {
        boolean success = noteService.addNoteToNotebook(request.getNoteId(), request.getNotebookId());
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        return ResponseEntity.ok(response);
    }
}

