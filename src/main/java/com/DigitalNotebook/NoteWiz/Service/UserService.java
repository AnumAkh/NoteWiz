package com.DigitalNotebook.NoteWiz.Service;

import com.DigitalNotebook.NoteWiz.Model.User;
import com.DigitalNotebook.NoteWiz.Repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    public String registerUser(User user) {
        if (!isValidEmail(user.getEmail())) {
            return "Invalid email format";
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already in use";
        }

        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        userRepository.save(user);

        return "User registered successfully";
    }

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return "User not found";
        }

        if (!BCrypt.checkpw(password, user.getPassword())) {
            return "Invalid password";
        }

        return "Login successful";
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
