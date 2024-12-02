package com.DigitalNotebook.NoteWiz.Service;

import com.DigitalNotebook.NoteWiz.Model.Note;
import com.DigitalNotebook.NoteWiz.Model.Notebook;
import com.DigitalNotebook.NoteWiz.Model.User;
import com.DigitalNotebook.NoteWiz.Repository.NoteRepository;
import com.DigitalNotebook.NoteWiz.Repository.NotebookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NotebookRepository notebookRepository;

    @Autowired
    private NotebookService notebookService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Find all notes for a user
    public List<Note> findNotesByUserId(int userId) {
        return noteRepository.findNotesByUserId(userId);
    }

    // Save a new note
    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }

    // Get all notes for a user
    public List<Note> getUserNotes(int userId) {
        return noteRepository.findNotesByUserId(userId);
    }

    public Note findNoteById(int noteId) {
        return noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found with ID: " + noteId));
    }

    public boolean deleteNoteByIdAndUser(int noteId, User currentUser) {
        // Check if the note exists
        Optional<Note> noteOptional = noteRepository.findById(noteId);
        if (noteOptional.isPresent()) {
            Note note = noteOptional.get();

            // Check if the note belongs to the current user
            if (note.getUser().getUserId() == currentUser.getUserId()) {
                // Delete the note
                noteRepository.delete(note);
                return true;
            }
        }
        return false;
    }

    public boolean addNoteToNotebook(int noteId, int notebookId) {
        return notebookService.addNoteToNotebook(noteId, notebookId);
    }

    public void shareNoteWithCollaborator(int noteId, int collaboratorId) {
        String sql = "INSERT INTO Note_Collaborators (note_id, user_id, collaborator_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, noteId, findNoteById(noteId).getUser().getUserId(), collaboratorId);
    }

    public List<Note> getSharedNotes(int userId) {
        return noteRepository.findSharedNotesByCollaboratorId(userId);
    }
}

