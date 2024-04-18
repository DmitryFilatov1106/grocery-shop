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
import ru.fildv.groceryshop.http.dto.category.CategoryEditDto;
import ru.fildv.groceryshop.http.dto.category.CategoryFilterDto;
import ru.fildv.groceryshop.http.dto.category.CategoryReadDto;
import ru.fildv.groceryshop.http.dto.page.PageResponseDto;
import ru.fildv.groceryshop.service.CategoryService;

import java.util.Objects;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public String findAll(@RequestParam(name = "page", required = false, defaultValue = "0") int pageNumber,
                          @ModelAttribute("categoryFilterDto") CategoryFilterDto filterDto,
                          Model model) {

        Page<CategoryReadDto> page = categoryService.findAll(filterDto, pageNumber);

        model.addAttribute("data", PageResponseDto.of(page));
        model.addAttribute("allPages", IntStream.range(0, page.getTotalPages()).toArray());
        model.addAttribute("mapping", "categories");
        model.addAttribute("filter", filterDto);

        if (Objects.isNull(filterDto.getName()))
            model.addAttribute("parfilter", "");
        else
            model.addAttribute("parfilter", "&name=" + filterDto.getName());

        return "category/categories";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Integer id, Model model) {
        return categoryService.findById(id)
                .map(category -> {
                    model.addAttribute("category", category);
                    return "category/category";
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Integer id,
                         @ModelAttribute @Validated CategoryEditDto category,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("category", category);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/categories/" + id;
        }
        return categoryService.update(id, category)
                .map(it -> "redirect:/categories/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        if (categoryService.delete(id))
            return "redirect:/categories";
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/add")
    public String create(Model model, @ModelAttribute("category") CategoryEditDto category) {
        model.addAttribute(category);
        return "category/category_add";
    }

    @PostMapping
    public String create(@ModelAttribute @Validated CategoryEditDto category,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("category", category);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/categories/add";
        }
        categoryService.create(category);
        return "redirect:/categories";
    }
}
