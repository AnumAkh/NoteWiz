package com.DigitalNotebook.NoteWiz.Service;

import com.DigitalNotebook.NoteWiz.Model.Note;
import com.DigitalNotebook.NoteWiz.Repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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

}

