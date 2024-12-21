package ox.techtaskoxcompany.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthFunctionalController {
    @GetMapping("/main-page")
    public String main() {
        return "mainpage";
    }

    @GetMapping("/add-client")
    public String addClient() {
        return "addclient";
    }

    @GetMapping("/add-task")
    public String addTask() {
        return "addtask";
    }

    @GetMapping("/add-contact")
    public String addContact() {
        return "addcontact";
    }
}
