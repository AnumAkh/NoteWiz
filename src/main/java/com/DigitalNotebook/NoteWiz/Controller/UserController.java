package com.DigitalNotebook.NoteWiz.Controller;

import com.DigitalNotebook.NoteWiz.Model.User;
import com.DigitalNotebook.NoteWiz.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Display the signup page
    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";  // Returns the signup view (signup.html or signup.jsp)
    }

    // Handle signup form submission
    @PostMapping("/signup")
    public String signup(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        String response = userService.registerUser(user);
        System.out.println("input taken from user" +user.getFirstName()+"  "+user.getPassword());

        if (response.equals("User registered successfully")) {
            redirectAttributes.addFlashAttribute("successMessage", "Signup successful! Please log in.");
            return "redirect:/login";  // Redirect to login page after successful signup
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", response);
            return "redirect:/signup";
        }
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, RedirectAttributes redirectAttributes) {
        String response = userService.loginUser(email, password);

        if (response.equals("Login successful")) {
            User loggedInUser = userService.getUserByEmail(email);

            session.setAttribute("loggedInUser", loggedInUser);
            redirectAttributes.addFlashAttribute("successMessage", "Login successful! Welcome back.");
            return "redirect:/home";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", response);
            return "redirect:/login";
        }
    }
}
