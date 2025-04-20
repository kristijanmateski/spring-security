package com.example.springsecurity.web;

import com.example.springsecurity.Model.dto.RegisterDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"", "/"})
    public String home(Model model) {
        model.addAttribute("user");
        return "index";
    }
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
}
