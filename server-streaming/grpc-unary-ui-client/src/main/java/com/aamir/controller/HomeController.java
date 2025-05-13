package com.aamir.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    /**
     * This method handles the GET request for the home page.
     * It returns the name of the view to be rendered.
     *
     * @return The name of the view to be rendered.
     */
    @GetMapping("/home")
    public String home() {
        return "index";
    }
}
