package com.example.google_drive_clone.controller;

import com.example.google_drive_clone.model.User;
import com.example.google_drive_clone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        System.out.println("Displaying login form.");
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        System.out.println("Displaying registration form.");
        return "register";
    }

    // POST mapping to handle the registration form submission
    @PostMapping("/register")
    public String registerUser(User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register"; // If there are validation errors, redisplay the registration form
        }
        try {
            userService.register(user); // Register the user
            return "redirect:/login?registered=true"; // Redirect to the login page with a success message
        } catch (Exception e) {
            model.addAttribute("registrationError", "An error occurred during registration. Please try again.");
            return "register"; // Redisplay the registration form with an error message
        }
    }

    @GetMapping("/index")
    public String showIndex(Model model) {
        System.out.println("Displaying index page.");
        return "index";
    }

    @GetMapping("/welcome")
    public String showWelcome(Model model) {
        System.out.println("Displaying welcome page.");
        return "welcome";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        System.out.println("Displaying Dashboard page.");
        return "Dashboard";
    }
}
