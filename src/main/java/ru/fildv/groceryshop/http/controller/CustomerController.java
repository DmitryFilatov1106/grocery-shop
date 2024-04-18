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
import ru.fildv.groceryshop.database.entity.enums.Status;
import ru.fildv.groceryshop.http.dto.address.AddressViewDto;
import ru.fildv.groceryshop.http.dto.customer.CustomerEditDto;
import ru.fildv.groceryshop.http.dto.customer.CustomerFilterDto;
import ru.fildv.groceryshop.http.dto.customer.CustomerSimpleReadDto;
import ru.fildv.groceryshop.http.dto.page.PageResponseDto;
import ru.fildv.groceryshop.service.CustomerService;
import ru.fildv.groceryshop.service.UserService;

import java.util.Objects;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Controller
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final UserService userService;

    @GetMapping
    public String findAll(@RequestParam(name = "page", required = false, defaultValue = "0") int pageNumber,
                          @ModelAttribute("customerFilterDto") CustomerFilterDto filterDto,
                          Model model) {

        Page<CustomerSimpleReadDto> page = customerService.findAll(filterDto, pageNumber);

        model.addAttribute("data", PageResponseDto.of(page));
        model.addAttribute("allPages", IntStream.range(0, page.getTotalPages()).toArray());
        model.addAttribute("mapping", "customers");
        model.addAttribute("statuses", Status.getAllNamesFilter());
        model.addAttribute("filter", filterDto);

        if (Objects.isNull(filterDto.getName()) && Objects.isNull(filterDto.getStatus()))
            model.addAttribute("parfilter", "");
        else
            model.addAttribute("parfilter", "&name=" + filterDto.getName()
                    + "&status=" + filterDto.getStatus().replace(' ', '+'));

        return "customer/customers";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Integer id, Model model) {
        return customerService.findByIdCustom(id)
                .map(customer -> {
                    model.addAttribute("customer", customer);
                    model.addAttribute("statuses", Status.getAllNames());
                    model.addAttribute("managers", userService.findAllManagers());
                    model.addAttribute("address", new AddressViewDto(
                            "addresses/" + id + "/customer",
                            customer.getAddress()));
                    return "customer/customer";
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Integer id,
                         @ModelAttribute @Validated CustomerEditDto customer,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("customer", customer);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/customers/" + id;
        }
        return customerService.update(id, customer)
                .map(it -> "redirect:/customers/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        if (customerService.delete(id))
            return "redirect:/customers";
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/add")
    public String create(Model model, @ModelAttribute("customer") CustomerEditDto customer) {
        model.addAttribute(customer);
        model.addAttribute("statuses", Status.getAllNames());
        model.addAttribute("managers", userService.findAllManagers());
        return "customer/customer_add";
    }

    @PostMapping
    public String create(@ModelAttribute @Validated CustomerEditDto customer,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("customer", customer);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/customers/add";
        }
        customerService.create(customer);
        return "redirect:/customers";
    }
}
