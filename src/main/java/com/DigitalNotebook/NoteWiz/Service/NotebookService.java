package com.DigitalNotebook.NoteWiz.Service;

import com.DigitalNotebook.NoteWiz.Model.Note;
import com.DigitalNotebook.NoteWiz.Model.Notebook;
import com.DigitalNotebook.NoteWiz.Repository.NoteRepository;
import com.DigitalNotebook.NoteWiz.Repository.NotebookRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class NotebookService {

    @Autowired
    private NotebookRepository notebookRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Notebook createNotebook(Notebook notebook) {
        return notebookRepository.save(notebook);
    }

    public List<Notebook> getAllNotebooks() {
        List<Notebook> notebooks = notebookRepository.findAll();
        notebooks.forEach(notebook -> Hibernate.initialize(notebook.getNotes()));
        return notebooks;
    }

    public List<Map<String, Object>> getNotesInNotebooksByUserId(int userId) {
        String sql = "SELECT n.notebook_title, nt.note_title, nt.content " +
                "FROM Notebooks n " +
                "LEFT JOIN Notes nt ON n.notebook_id = nt.notebook_id " +
                "WHERE n.user_id = ? " +
                "ORDER BY n.notebook_title";
        return jdbcTemplate.queryForList(sql, userId);
    }

    public List<Notebook> getUserNotebooksWithNotes(int userId) {

    List<Notebook> notebooks = notebookRepository.findByUser_UserId(userId);

    for (Notebook notebook : notebooks) {

        String sql = "SELECT nt.* " +
                "FROM Notes nt " +
                "INNER JOIN notebooks_notes nn ON nt.note_id = nn.note_id " +
                "WHERE nn.notebook_id = ?";
        List<Note> notes = jdbcTemplate.query(sql, new Object[]{notebook.getNotebookId()},
                (rs, rowNum) -> new Note(
                        rs.getInt("note_id"),
                        rs.getString("note_title"),
                        rs.getBytes("content_blob"),
                        rs.getString("content"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                        null,
                        null,
                        null
                )
        );
        notebook.setNotes(notes);
    }

    return notebooks;
}
    public boolean addNoteToNotebook(int noteId, int notebookId) {
        String sql = "INSERT INTO notebooks_notes (note_id, notebook_id) VALUES (?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, noteId, notebookId);
        return rowsAffected > 0;
    }
}

