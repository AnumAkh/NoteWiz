package com.DigitalNotebook.NoteWiz.Model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class NoteCollaboratorId implements Serializable {

    private int noteId;
    private int collaboratorId;
}