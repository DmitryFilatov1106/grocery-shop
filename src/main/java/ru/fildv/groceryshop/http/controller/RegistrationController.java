package ru.fildv.groceryshop.http.controller;

import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.fildv.groceryshop.database.entity.enums.Role;
import ru.fildv.groceryshop.http.dto.user.UserEditDto;
import ru.fildv.groceryshop.http.validation.group.CreateAction;
import ru.fildv.groceryshop.service.UserService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;

    @GetMapping
    public String registration(Model model, @ModelAttribute("user") UserEditDto user) {
        model.addAttribute(user);
        model.addAttribute("role", Role.USER.name());
        return "security/registration";
    }

    @PostMapping
    public String registration(@ModelAttribute @Validated({Default.class, CreateAction.class}) UserEditDto user,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/registration";
        }
        userService.create(user);
        return "redirect:/login";
    }
}
