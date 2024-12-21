package ox.techtaskoxcompany.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class LoginRegController {

    @GetMapping("/login-registration")
    public String login() {
        return "loginregistration";
    }
}
