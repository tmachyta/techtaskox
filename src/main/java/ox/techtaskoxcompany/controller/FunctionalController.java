package ox.techtaskoxcompany.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class FunctionalController {
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/login-registration")
    public String login() {
        return "loginregistration";
    }
}
