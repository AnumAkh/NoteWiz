package com.DigitalNotebook.NoteWiz.Controller;

import com.DigitalNotebook.NoteWiz.Model.User;
import com.DigitalNotebook.NoteWiz.Service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public String showProfilePage(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in to access your profile.");
            return "redirect:/login";
        }
        model.addAttribute("user", loggedInUser);
        return "profile";
    }

    @PostMapping("/update/username")
    public String updateUsername(@RequestParam("firstName") String firstName,
                                 @RequestParam("lastName") String lastName,
                                 HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in to update your username.");
            return "redirect:/login";
        }

        loggedInUser.setFirstName(firstName);
        loggedInUser.setLastName(lastName);
        profileService.updateUser(loggedInUser);
        session.setAttribute("loggedInUser", loggedInUser);

        redirectAttributes.addFlashAttribute("successMessage", "Username updated successfully.");
        return "redirect:/profile"; // Redirect back to profile page
    }

    @PostMapping("/update/email")
    public String updateEmail(@RequestParam("newEmail") String newEmail,
                              HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in to update your email.");
            return "redirect:/login";
        }

        profileService.updateEmail(loggedInUser.getUserId(), newEmail);
        loggedInUser.setEmail(newEmail); // Update session data
        session.setAttribute("loggedInUser", loggedInUser);

        redirectAttributes.addFlashAttribute("successMessage", "Email updated successfully.");
        return "redirect:/profile";
    }

    @PostMapping("/update/password")
    public String updatePassword(@RequestParam("newPassword") String newPassword,
                                 HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in to update your password.");
            return "redirect:/login";
        }

        profileService.updatePassword(loggedInUser.getUserId(), newPassword);
        redirectAttributes.addFlashAttribute("successMessage", "Password updated successfully.");
        return "redirect:/profile";
    }
}
