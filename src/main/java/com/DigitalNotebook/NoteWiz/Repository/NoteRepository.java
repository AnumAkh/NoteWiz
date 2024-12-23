package com.DigitalNotebook.NoteWiz.Repository;

import com.DigitalNotebook.NoteWiz.Model.Note;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {

    @Query("SELECT n FROM Note n WHERE n.user.userId = :userId ORDER BY n.createdAt DESC")
    List<Note> findNotesByUserId(@Param("userId") int userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Note n WHERE n.noteId = :noteId AND n.user.userId = :userId")
    int deleteByNoteIdAndUserId(@Param("noteId") int noteId, @Param("userId") int userId);

    @Query("SELECT n FROM Note n JOIN NoteCollaborator nc ON n.noteId = nc.id.noteId WHERE nc.id.collaboratorId = :userId")
    List<Note> findSharedNotesByCollaboratorId(@Param("userId") int userId);
}

