package com.example.google_drive_clone.controller;

import com.example.google_drive_clone.model.User;
import com.example.google_drive_clone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // This handles the form submission for registering a user
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("registrationError", "There were errors in your registration.");
            return "register";
        }
        try {
            userService.register(user);
            return "redirect:/login?registered=true"; // Redirects to login with a success message
        } catch (Exception e) {
            model.addAttribute("registrationError", "An error occurred while creating your account. Please try again.");
            return "register";
        }
    }

    // Add other user-related endpoints as needed
}
