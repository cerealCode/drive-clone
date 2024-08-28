package com.example.google_drive_clone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "dashboard";  // This should match the name of the Thymeleaf template without the `.html` extension
    }
}
