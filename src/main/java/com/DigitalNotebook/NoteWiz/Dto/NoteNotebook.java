package com.DigitalNotebook.NoteWiz.Dto;

import lombok.Getter;

public class NoteNotebook {
    @Getter
    private int noteId;
    private int notebookId;

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getNotebookId() {
        return notebookId;
    }

    public void setNotebookId(int notebookId) {
        this.notebookId = notebookId;
    }
}
