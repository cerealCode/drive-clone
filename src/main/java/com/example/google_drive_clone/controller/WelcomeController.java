package com.example.google_drive_clone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/welcome")
public class WelcomeController {

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome"; // Ensure this corresponds to the correct view (welcome.html)
    }
}
