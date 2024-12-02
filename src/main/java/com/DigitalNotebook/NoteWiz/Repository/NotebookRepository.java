package com.DigitalNotebook.NoteWiz.Repository;

import com.DigitalNotebook.NoteWiz.Model.Notebook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotebookRepository extends JpaRepository<Notebook, Integer> {
    List<Notebook> findByUser_UserId(int userId);

    Optional<Notebook> findByNotebookTitle(String notebookTitle);
}
