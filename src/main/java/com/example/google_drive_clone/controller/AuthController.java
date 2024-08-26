package com.example.google_drive_clone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class AuthController {

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        System.out.println("Displaying login form.");
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        System.out.println("Displaying registration form.");
        return "register";
    }

    @GetMapping("/index")
    public String showIndex(Model model) {
        System.out.println("Displaying index page.");
        return "index";
    }
}
