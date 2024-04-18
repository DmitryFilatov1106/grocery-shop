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
import ru.fildv.groceryshop.http.dto.provider_order.ProviderOrderEditDto;
import ru.fildv.groceryshop.http.dto.provider_order.ProviderOrderFilterDto;
import ru.fildv.groceryshop.http.dto.provider_order.ProviderOrderReadDto;
import ru.fildv.groceryshop.service.ProviderOrderLineService;
import ru.fildv.groceryshop.service.ProviderOrderService;

import java.util.Objects;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Controller
@RequestMapping("/providerorders")
public class ProviderOrderController {
    private final ProviderOrderService providerOrderService;
    private final ProviderOrderLineService providerOrderLineService;

    @GetMapping
    public String findAll(@RequestParam(name = "page", required = false, defaultValue = "0") int pageNumber,
                          @ModelAttribute("providerOrderFilterDto") ProviderOrderFilterDto filterDto,
                          Model model) {

        Page<ProviderOrderReadDto> page = providerOrderService.findAll(filterDto, pageNumber);

        model.addAttribute("data", PageResponseDto.of(page));
        model.addAttribute("allPages", IntStream.range(0, page.getTotalPages()).toArray());
        model.addAttribute("mapping", "providerorders");
        model.addAttribute("filter", filterDto);

        if (Objects.isNull(filterDto.getId())
                && Objects.isNull(filterDto.getFromOrderDate())
                && Objects.isNull(filterDto.getToOrderDate())
                && Objects.isNull(filterDto.getComment()))
            model.addAttribute("parfilter", "");
        else {
            var parId = Objects.isNull(filterDto.getId()) ? "" : filterDto.getId();
            var parFromOrderDate = Objects.isNull(filterDto.getFromOrderDate()) ? "" : filterDto.getFromOrderDate();
            var parToOrderDate = Objects.isNull(filterDto.getToOrderDate()) ? "" : filterDto.getToOrderDate();
            var parComment = Objects.isNull(filterDto.getComment()) ? "" : filterDto.getComment();

            model.addAttribute("parfilter", "&id=" + parId
                    + "&fromOrderDate=" + parFromOrderDate
                    + "&toOrderDate=" + parToOrderDate
                    + "&comment=" + parComment
            );
        }

        return "provider_order/provider_orders";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Integer id, Model model) {
        return providerOrderService.findById(id)
                .map(order -> {
                    model.addAttribute("order", order);
                    model.addAttribute("orderlines", providerOrderLineService.findAllByProviderOrderId(order.getId()));
                    return "provider_order/provider_order";
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Integer id,
                         @ModelAttribute @Validated ProviderOrderEditDto order,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("order", order);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/providerorders/{id}";
        }
        return providerOrderService.update(id, order)
                .map(it -> "redirect:/providerorders/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        if (providerOrderService.delete(id))
            return "redirect:/providerorders";
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/add")
    public String create(Model model, @ModelAttribute("order") ProviderOrderEditDto order) {
        model.addAttribute(order);
        return "provider_order/provider_order_add";
    }

    @PostMapping
    public String create(@ModelAttribute @Validated ProviderOrderEditDto order,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("order", order);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/providerorders/add";
        }
        var orderDto = providerOrderService.create(order);
        return "redirect:/providerorders/" + orderDto.getId();
    }
}
