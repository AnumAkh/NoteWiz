package com.DigitalNotebook.NoteWiz.Model;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "Notes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id")
    private int noteId;

    @NotNull
    @Column(name = "note_title", nullable = false)
    private String noteTitle;

    @NotNull
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @ManyToOne // Each note belongs to one notebook
    @JoinColumn(name = "notebook_id", referencedColumnName = "notebook_id")
    private Notebook notebook; // Association with the Notebook entity

    @ManyToOne // Each note is created by a user
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user; // Association with the User entity (creator)

    // Lombok will automatically generate constructors, getters, and setters
}
