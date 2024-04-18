package ru.fildv.groceryshop.http.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import ru.fildv.groceryshop.service.UserService;

@RequiredArgsConstructor
@Controller
public class LoginController {
    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "security/login";
    }

    @GetMapping("/start")
    public String mainPage() {
        return "security/start";
    }

    @GetMapping("/changepassword")
    public String changePassword(Model model) {
        model.addAttribute("password", "");
        return "security/changepassword";
    }

    @PostMapping("/changepassword")
    public String changePassword(@ModelAttribute("password") String password) {
        if (userService.setPassword(password)) {
            return "redirect:/start";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
