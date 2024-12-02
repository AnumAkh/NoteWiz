package com.DigitalNotebook.NoteWiz.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tagId;

    private String tagName;

    @ManyToOne
    @JoinColumn(name = "note_id", referencedColumnName = "note_id")
    private Note note; // Many-to-One relationship with Note

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user; // Many-to-One relationship with User

    // Getters and Setters
}
