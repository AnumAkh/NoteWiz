package com.DigitalNotebook.NoteWiz.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="forum_posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int post_id;

    @Column(name = "post_title", nullable = false, length = 200)
    private String postTitle;

    @Column(name = "post_content", columnDefinition = "LONGTEXT")
    private String postContent;

    @Column(name = "created_on", nullable = false, updatable = false)
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "total_upvote", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer totalUpvote = 0;

    @Column(name = "total_downvote", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer totalDownvote = 0;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user; // The user who created the post

    @Transient
    private String postAuthorName;

    @Transient
    private Integer commentCount;

    @Setter
    @Getter
    @Transient
    private String formattedCreatedOn;

    @Transient
    private List<ForumReply> replies;

}

