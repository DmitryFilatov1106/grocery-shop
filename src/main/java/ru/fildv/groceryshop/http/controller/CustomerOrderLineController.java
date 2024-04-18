package ru.fildv.groceryshop.http.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.fildv.groceryshop.http.dto.customer_order_line.CustomerOrderLineEditDto;
import ru.fildv.groceryshop.service.*;

import java.util.Objects;

@RequiredArgsConstructor
@Controller
@RequestMapping("/customerorderlines")
@PreAuthorize("hasAnyAuthority({'ADMIN', 'MANAGER'})")
public class CustomerOrderLineController {
    private final CustomerOrderLineService customerOrderLineService;
    private final CustomerOrderService customerOrderService;
    private final ProductService productService;
    private final QualityService qualityService;
    private final ProductUnitService productUnitService;
    private final CustomerService customerService;
    private final ProjectService projectService;

    @GetMapping("/{customerOrderId}/add")
    public String create(Model model,
                         @PathVariable("customerOrderId") Integer customerOrderId,
                         @ModelAttribute("orderLine") CustomerOrderLineEditDto orderLine) {
        var productNull = Objects.isNull(orderLine.getProductId());
        model.addAttribute(orderLine);
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("projects", projectService.findAll());
        customerOrderService.findById(customerOrderId)
                .map(orderDto -> model.addAttribute("order", orderDto))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("orderlines", customerOrderLineService.findAllByCustomerOrderId(customerOrderId));
        if (productNull) {
            model.addAttribute("products", productService.findPart());
            return "customer_order_line/customer_order_line_add";
        } else {
            productService.findById(orderLine.getProductId())
                    .map(productDto -> model.addAttribute("product", productDto))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            model.addAttribute("productUnits", productUnitService.findAllByProductId(orderLine.getProductId()));
            model.addAttribute("qualities", qualityService.findAll());
            return "customer_order_line/customer_order_line_add2";
        }
    }

    @PostMapping
    public String create(@ModelAttribute @Validated CustomerOrderLineEditDto orderLine,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("orderLine", orderLine);
            redirectAttributes.addFlashAttribute("errorsLine", bindingResult.getAllErrors());
            return "redirect:/customerorderlines/" + orderLine.getCustomerOrderId() + "/add";
        }
        customerOrderLineService.create(orderLine);
        return "redirect:/customerorders/" + orderLine.getCustomerOrderId();
    }

    @GetMapping("/{customerOrderId}/{id}/delete")
    public String delete(@PathVariable("customerOrderId") Integer customeroOrderId, @PathVariable("id") Integer id) {
        if (customerOrderLineService.delete(id))
            return "redirect:/customerorders/" + customeroOrderId;
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
