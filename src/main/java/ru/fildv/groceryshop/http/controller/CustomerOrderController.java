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
import ru.fildv.groceryshop.exception.StockEmpty;
import ru.fildv.groceryshop.http.dto.customer_order.CustomerOrderEditDto;
import ru.fildv.groceryshop.http.dto.customer_order.CustomerOrderFilterDto;
import ru.fildv.groceryshop.http.dto.customer_order.CustomerOrderSimpleReadDto;
import ru.fildv.groceryshop.http.dto.page.PageResponseDto;
import ru.fildv.groceryshop.service.CustomerOrderLineService;
import ru.fildv.groceryshop.service.CustomerOrderService;
import ru.fildv.groceryshop.service.CustomerService;
import ru.fildv.groceryshop.service.ProjectService;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Controller
@RequestMapping("/customerorders")
public class CustomerOrderController {
    private final CustomerOrderService customerOrderService;
    private final CustomerOrderLineService customerOrderLineService;
    private final CustomerService customerService;
    private final ProjectService projectService;

    @GetMapping
    public String findAll(@RequestParam(name = "page", required = false, defaultValue = "0") int pageNumber,
                          @ModelAttribute("customerOrderFilterDto") CustomerOrderFilterDto filterDto,
                          Model model) {

        Page<CustomerOrderSimpleReadDto> page = customerOrderService.findAll(filterDto, pageNumber);

        model.addAttribute("data", PageResponseDto.of(page));
        model.addAttribute("allPages", IntStream.range(0, page.getTotalPages()).toArray());
        model.addAttribute("mapping", "customerorders");
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
        return "customer_order/customer_orders";
    }

    @GetMapping("/{id}")
    public String findById(@RequestParam(name = "error", required = false) String error,
                           @PathVariable Integer id,
                           Model model) {
        return customerOrderService.findById(id)
                .map(order -> {
                    model.addAttribute("error", error);
                    model.addAttribute("order", order);
                    model.addAttribute("customers", customerService.findAll());
                    model.addAttribute("projects", projectService.findAll());
                    model.addAttribute("orderlines", customerOrderLineService.findAllByCustomerOrderId(order.getId()));
                    return "customer_order/customer_order";
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Integer id,
                         @ModelAttribute @Validated CustomerOrderEditDto order,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("order", order);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/customerorders/{id}";
        }
        try {
            return customerOrderService.update(id, order)
                    .map(it -> "redirect:/customerorders/{id}")
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        } catch (StockEmpty e) {
            return "redirect:/customerorders/{id}?error=" + Arrays.toString(e.getDeficiency().toArray())
                    .replace("[", "")
                    .replace("]", "")
                    .replace(" ", "");
        }
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        if (customerOrderService.delete(id))
            return "redirect:/customerorders";
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/add")
    public String create(Model model, @ModelAttribute("order") CustomerOrderEditDto order) {
        model.addAttribute(order);
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("projects", projectService.findAll());
        return "customer_order/customer_order_add";
    }

    @PostMapping
    public String create(@ModelAttribute @Validated CustomerOrderEditDto order,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("order", order);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/customerorders/add";
        }
        var orderDto = customerOrderService.create(order);
        return "redirect:/customerorders/" + orderDto.getId();
    }
}
