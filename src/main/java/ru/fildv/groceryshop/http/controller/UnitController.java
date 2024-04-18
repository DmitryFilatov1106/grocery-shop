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
import ru.fildv.groceryshop.http.dto.unit.UnitEditDto;
import ru.fildv.groceryshop.http.dto.unit.UnitFilterDto;
import ru.fildv.groceryshop.http.dto.unit.UnitReadDto;
import ru.fildv.groceryshop.service.UnitService;

import java.util.Objects;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Controller
@RequestMapping("/units")
public class UnitController {
    private final UnitService unitService;

    @GetMapping
    public String findAll(@RequestParam(name = "page", required = false, defaultValue = "0") int pageNumber,
                          @ModelAttribute("unitFilterDto") UnitFilterDto filterDto,
                          Model model) {

        Page<UnitReadDto> page = unitService.findAll(filterDto, pageNumber);

        model.addAttribute("data", PageResponseDto.of(page));
        model.addAttribute("allPages", IntStream.range(0, page.getTotalPages()).toArray());
        model.addAttribute("mapping", "units");
        model.addAttribute("filter", filterDto);

        if (Objects.isNull(filterDto.getName()))
            model.addAttribute("parfilter", "");
        else
            model.addAttribute("parfilter", "&name=" + filterDto.getName());

        return "unit/units";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Integer id, Model model) {
        return unitService.findById(id)
                .map(unit -> {
                    model.addAttribute("unit", unit);
                    return "unit/unit";
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Integer id,
                         @ModelAttribute @Validated UnitEditDto unit,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("unit", unit);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/units/" + id;
        }
        return unitService.update(id, unit)
                .map(it -> "redirect:/units/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        if (unitService.delete(id))
            return "redirect:/units";
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/add")
    public String create(Model model, @ModelAttribute("unit") UnitEditDto unit) {
        model.addAttribute(unit);
        return "unit/unit_add";
    }

    @PostMapping
    public String create(@ModelAttribute @Validated UnitEditDto unit,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("unit", unit);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/units/add";
        }
        unitService.create(unit);
        return "redirect:/units";
    }
}
