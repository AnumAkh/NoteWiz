package com.DigitalNotebook.NoteWiz.Service;

import com.DigitalNotebook.NoteWiz.Model.ForumPost;
import com.DigitalNotebook.NoteWiz.Model.User;
import com.DigitalNotebook.NoteWiz.Repository.ForumPostRepository;
import com.DigitalNotebook.NoteWiz.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ForumPostService {

    @Autowired
    private ForumPostRepository forumPostRepository;
    @Autowired
    private UserRepository userRepository;

//create
@Transactional
public ForumPost createPost(ForumPost forumPost,User user) {

    forumPost.setUser(user);
    forumPost.setCreatedOn(LocalDateTime.now());
    forumPost.setTotalUpvote(0);
    forumPost.setTotalDownvote(0);

    return forumPostRepository.save(forumPost);
}
//    public List<ForumPost>  getAllPost(){
//
//    }
//    public ForumPost getPostById(Integer Post_id){
//
//    }
//    public List<ForumPost> getAllPostByUser(Integer user_id){
//
//    }
//    public List<ForumPost> searchPost(String Keyword){
//
//    }
}
