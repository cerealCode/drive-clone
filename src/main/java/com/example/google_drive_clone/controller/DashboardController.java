package com.example.google_drive_clone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;


@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Add any required model attributes here
        return "dashboard"; // Returns the Thymeleaf view named "dashboard.html"
    }
}
