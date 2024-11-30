package com.DigitalNotebook.NoteWiz.Model;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Notebooks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notebook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notebook_id")
    private int notebookId;

    @NotNull
    @Column(name = "notebook_title", nullable = false)
    private String notebookTitle;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "updated_on", nullable = true)
    private LocalDateTime updatedOn;

    @OneToMany
    private List<Note> notes; // One-to-many relationship with notes

    @ManyToOne // Each notebook belongs to one user (creator)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user; // Association with the User entity (creator)
}
