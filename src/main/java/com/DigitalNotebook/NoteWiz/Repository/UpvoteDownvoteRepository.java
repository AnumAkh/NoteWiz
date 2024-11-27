package com.DigitalNotebook.NoteWiz.Repository;

import com.DigitalNotebook.NoteWiz.Model.UpvoteDownvote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpvoteDownvoteRepository extends JpaRepository<UpvoteDownvote,Integer> {
}
