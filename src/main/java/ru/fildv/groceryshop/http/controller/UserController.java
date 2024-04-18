package ru.fildv.groceryshop.http.controller;

import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.fildv.groceryshop.database.entity.enums.Role;
import ru.fildv.groceryshop.http.dto.address.AddressViewDto;
import ru.fildv.groceryshop.http.dto.page.PageResponseDto;
import ru.fildv.groceryshop.http.dto.user.UserEditDto;
import ru.fildv.groceryshop.http.dto.user.UserFilterDto;
import ru.fildv.groceryshop.http.dto.user.UserReadDto;
import ru.fildv.groceryshop.http.validation.group.CreateAction;
import ru.fildv.groceryshop.http.validation.group.UpdateAction;
import ru.fildv.groceryshop.service.UserService;

import java.util.Objects;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    private static String[] getRoles(boolean withBlank) {
        var roleValues = Role.values();
        String[] roles;
        int i;
        if (withBlank) {
            roles = new String[roleValues.length + 1];
            roles[0] = "";
            i = 0;
        } else {
            roles = new String[roleValues.length];
            i = -1;
        }
        for (Role r : roleValues) roles[++i] = r.name();
        return roles;
    }

    @GetMapping
    public String findAll(@RequestParam(name = "page", required = false, defaultValue = "0") int pageNumber,
                          @ModelAttribute("userFilterDto") UserFilterDto filterDto,
                          Model model) {

        Page<UserReadDto> page = userService.findAll(filterDto, pageNumber);

        model.addAttribute("data", PageResponseDto.of(page));
        model.addAttribute("allPages", IntStream.range(0, page.getTotalPages()).toArray());
        model.addAttribute("mapping", "users");
        model.addAttribute("filter", filterDto);

        model.addAttribute("roles", getRoles(true));

        if (Objects.isNull(filterDto.getUsername())
                && Objects.isNull(filterDto.getFirstname())
                && Objects.isNull(filterDto.getLastname())
                && Objects.isNull(filterDto.getRole()))
            model.addAttribute("parfilter", "");
        else {
            var parUsername = Objects.isNull(filterDto.getUsername()) ? "" : filterDto.getUsername();
            var parFirstname = Objects.isNull(filterDto.getFirstname()) ? "" : filterDto.getFirstname();
            var parLastname = Objects.isNull(filterDto.getLastname()) ? "" : filterDto.getLastname();
            var parRole = Objects.isNull(filterDto.getRole()) ? "" : filterDto.getRole();

            model.addAttribute("parfilter", "&username=" + parUsername
                    + "&firstname=" + parFirstname
                    + "&lastname=" + parLastname
                    + "&role=" + parRole
            );
        }
        return "user/users";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Integer id, Model model) {
        return userService.findById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    model.addAttribute("roles", getRoles(false));
                    model.addAttribute("address", new AddressViewDto(
                            "addresses/" + id + "/user",
                            user.getAddress()));
                    return "user/user";
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Integer id,
                         @ModelAttribute @Validated({Default.class, UpdateAction.class}) UserEditDto user,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/users/" + id;
        }
        return userService.update(id, user)
                .map(it -> "redirect:/users/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        if (userService.delete(id))
            return "redirect:/users";
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/add")
    public String create(Model model, @ModelAttribute("user") UserEditDto user) {
        model.addAttribute(user);
        model.addAttribute("roles", Role.values());
        return "user/user_add";
    }

    @PostMapping
    public String create(@ModelAttribute @Validated({Default.class, CreateAction.class}) UserEditDto user,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/users/add";
        }
        userService.create(user);
        return "redirect:/users";
    }
}
