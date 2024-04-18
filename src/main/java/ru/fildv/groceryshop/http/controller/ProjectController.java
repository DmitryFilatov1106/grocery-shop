package ru.fildv.groceryshop.http.controller;

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
import ru.fildv.groceryshop.http.dto.page.PageResponseDto;
import ru.fildv.groceryshop.http.dto.project.ProjectEditDto;
import ru.fildv.groceryshop.http.dto.project.ProjectFilterDto;
import ru.fildv.groceryshop.http.dto.project.ProjectReadDto;
import ru.fildv.groceryshop.service.ProjectService;
import ru.fildv.groceryshop.service.UserService;

import java.util.Objects;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Controller
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final UserService userService;

    @GetMapping
    public String findAll(@RequestParam(name = "page", required = false, defaultValue = "0") int pageNumber,
                          @ModelAttribute("projectFilterDto") ProjectFilterDto filterDto,
                          Model model) {

        Page<ProjectReadDto> page = projectService.findAll(filterDto, pageNumber);

        model.addAttribute("data", PageResponseDto.of(page));
        model.addAttribute("allPages", IntStream.range(0, page.getTotalPages()).toArray());
        model.addAttribute("mapping", "projects");
        model.addAttribute("filter", filterDto);

        if (Objects.isNull(filterDto.getName()))
            model.addAttribute("parfilter", "");
        else
            model.addAttribute("parfilter", "&name=" + filterDto.getName());

        return "project/projects";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Integer id, Model model) {
        return projectService.findByIdCustom(id)
                .map(project -> {
                    model.addAttribute("project", project);
                    model.addAttribute("promanagers", userService.findAllProManagers());
                    return "project/project";
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Integer id,
                         @ModelAttribute @Validated ProjectEditDto project,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("project", project);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/projects/" + id;
        }
        return projectService.update(id, project)
                .map(it -> "redirect:/projects/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        if (projectService.delete(id))
            return "redirect:/projects";
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/add")
    public String create(Model model, @ModelAttribute("project") ProjectEditDto project) {
        model.addAttribute(project);
        model.addAttribute("promanagers", userService.findAllProManagers());
        return "project/project_add";
    }

    @PostMapping
    public String create(@ModelAttribute @Validated ProjectEditDto project,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("project", project);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/projects/add";
        }
        projectService.create(project);
        return "redirect:/projects";
    }
}
