package com.DigitalNotebook.NoteWiz.Repository;

import com.DigitalNotebook.NoteWiz.Model.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;
import com.DigitalNotebook.NoteWiz.Model.User;

import java.util.List;

public interface ForumPostRepository extends JpaRepository<ForumPost,Integer> {
       // List<ForumPost> getAllPost();
      //  List<ForumPost> getAllByUser(User user);


}
