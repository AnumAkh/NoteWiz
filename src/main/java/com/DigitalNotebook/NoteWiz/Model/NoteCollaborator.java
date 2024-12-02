package com.DigitalNotebook.NoteWiz.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "note_collaborators")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteCollaborator {

    @EmbeddedId
    private NoteCollaboratorId id;

    @ManyToOne
    @MapsId("noteId")
    @JoinColumn(name = "note_id", referencedColumnName = "note_id")
    private Note note;

    @ManyToOne
    @MapsId("collaboratorId")
    @JoinColumn(name = "collaborator_id", referencedColumnName = "user_id")
    private User collaborator;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User owner;
}