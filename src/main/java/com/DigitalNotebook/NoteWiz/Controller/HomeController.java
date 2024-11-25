package com.DigitalNotebook.NoteWiz.Controller;

import com.DigitalNotebook.NoteWiz.Model.User;
import com.DigitalNotebook.NoteWiz.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String getHomePage(Model model) {
        User user = userService.getUserById(1); // Example of fetching a user
        model.addAttribute("user", user);
        model.addAttribute("title","Home Page");
        return "home"; // home.html in templates folder
    }
}
