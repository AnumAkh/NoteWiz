package com.DigitalNotebook.NoteWiz.Controller;

import com.DigitalNotebook.NoteWiz.Model.User;
import com.DigitalNotebook.NoteWiz.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

//    @GetMapping("/home")
//    public String getHomePage(Model model) {
//        User user = userService.getUserById(1); // Example of fetching a user
//        model.addAttribute("user", user);
//
//        return "home"; // home.html in templates folder
//    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {

        // retrieves the loggedInUser session attributes and adds it to the model as user

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", loggedInUser);
        model.addAttribute("title","Home Page");
        return "home"; // Return the home view
    }

    @GetMapping("/addNote")
    public String showAddNoteForm() {
        return "addNote";
    }
}
