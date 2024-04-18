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
import ru.fildv.groceryshop.http.dto.quality.QualityEditDto;
import ru.fildv.groceryshop.http.dto.quality.QualityFilterDto;
import ru.fildv.groceryshop.http.dto.quality.QualityReadDto;
import ru.fildv.groceryshop.service.QualityService;

import java.util.Objects;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Controller
@RequestMapping("/qualities")
public class QualityController {
    private final QualityService qualityService;

    @GetMapping
    public String findAll(@RequestParam(name = "page", required = false, defaultValue = "0") int pageNumber,
                          @ModelAttribute("qualityFilterDto") QualityFilterDto filterDto,
                          Model model) {
        Page<QualityReadDto> page = qualityService.findAll(filterDto, pageNumber);

        model.addAttribute("data", PageResponseDto.of(page));
        model.addAttribute("allPages", IntStream.range(0, page.getTotalPages()).toArray());
        model.addAttribute("mapping", "qualities");
        model.addAttribute("filter", filterDto);

        if (Objects.isNull(filterDto.getName()))
            model.addAttribute("parfilter", "");
        else
            model.addAttribute("parfilter", "&name=" + filterDto.getName());

        return "quality/qualities";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Integer id, Model model) {
        return qualityService.findById(id)
                .map(quality -> {
                    model.addAttribute("quality", quality);
                    return "quality/quality";
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Integer id,
                         @ModelAttribute @Validated QualityEditDto quality,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("quality", quality);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/qualities/{id}";
        }
        return qualityService.update(id, quality)
                .map(it -> "redirect:/qualities/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        if (qualityService.delete(id))
            return "redirect:/qualities";
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/add")
    public String create(Model model, @ModelAttribute("quality") QualityEditDto quality) {
        model.addAttribute(quality);
        return "quality/quality_add";
    }

    @PostMapping
    public String create(@ModelAttribute @Validated QualityEditDto quality,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("quality", quality);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/qualities/add";
        }
        qualityService.create(quality);
        return "redirect:/qualities";
    }
}
