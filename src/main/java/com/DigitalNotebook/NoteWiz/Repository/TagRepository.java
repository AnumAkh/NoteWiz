package com.DigitalNotebook.NoteWiz.Repository;

import com.DigitalNotebook.NoteWiz.Model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Query("SELECT t FROM Tag t WHERE t.note.user.userId = :userId")
    List<Tag> findTagsByUserId(@Param("userId") int userId);

}
