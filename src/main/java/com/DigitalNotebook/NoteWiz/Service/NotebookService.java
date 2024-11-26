package com.DigitalNotebook.NoteWiz.Service;

import com.DigitalNotebook.NoteWiz.Model.Notebook;
import com.DigitalNotebook.NoteWiz.Repository.NotebookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotebookService {

    @Autowired
    private NotebookRepository notebookRepository;

    public Notebook createNotebook(Notebook notebook) {
        return notebookRepository.save(notebook);
    }
}
