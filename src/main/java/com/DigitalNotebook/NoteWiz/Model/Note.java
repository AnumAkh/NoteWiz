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

    @Column(name = "content_blob")
    private byte[] contentBlob; // For handling 'pdf' or 'image' content

    @NotNull
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content; // The text content for text notes

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user; // The user who created the note
}
