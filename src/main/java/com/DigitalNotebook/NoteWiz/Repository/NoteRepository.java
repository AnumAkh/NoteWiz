package com.DigitalNotebook.NoteWiz.Repository;

import com.DigitalNotebook.NoteWiz.Model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {

    @Query("SELECT n FROM Note n WHERE n.user.userId = :userId ORDER BY n.createdAt DESC")
    List<Note> findNotesByUserId(@Param("userId") int userId);

}

