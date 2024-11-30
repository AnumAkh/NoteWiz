package com.DigitalNotebook.NoteWiz.Service;

import com.DigitalNotebook.NoteWiz.Model.Note;
import com.DigitalNotebook.NoteWiz.Model.User;
import com.DigitalNotebook.NoteWiz.Repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

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

        // If the note doesn't exist or doesn't belong to the current user, return false
        return false;
    }
}

