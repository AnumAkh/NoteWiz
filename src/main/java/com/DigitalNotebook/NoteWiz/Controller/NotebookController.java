package com.DigitalNotebook.NoteWiz.Controller;

import com.DigitalNotebook.NoteWiz.Model.Notebook;
import com.DigitalNotebook.NoteWiz.Model.User;
import com.DigitalNotebook.NoteWiz.Service.NotebookService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/create-notebooks")
public class NotebookController {

    @Autowired
    private NotebookService notebookService;

    @PostMapping
    public ResponseEntity<Notebook> createNotebook(@RequestBody Notebook notebook, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(null);
            // Unauthorized if no user is logged in
        }

        notebook.setUser(loggedInUser);
        notebook.setCreatedOn(LocalDateTime.now());

        Notebook savedNotebook = notebookService.createNotebook(notebook);
        return new ResponseEntity<>(savedNotebook, HttpStatus.CREATED);
    }
}

