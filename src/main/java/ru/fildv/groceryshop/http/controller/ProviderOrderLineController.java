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
import ru.fildv.groceryshop.http.dto.provider_order_line.ProviderOrderLineEditDto;
import ru.fildv.groceryshop.service.*;

import java.util.Objects;

@RequiredArgsConstructor
@Controller
@RequestMapping("/providerorderlines")
@PreAuthorize("hasAnyAuthority({'ADMIN', 'STOREKEEPER'})")
public class ProviderOrderLineController {
    private final ProviderOrderLineService providerOrderLineService;
    private final ProviderOrderService providerOrderService;
    private final ProductService productService;
    private final QualityService qualityService;
    private final ProductUnitService productUnitService;

    @GetMapping("/{providerOrderId}/add")
    public String create(Model model,
                         @PathVariable("providerOrderId") Integer providerOrderId,
                         @ModelAttribute("orderLine") ProviderOrderLineEditDto orderLine) {
        var productNull = Objects.isNull(orderLine.getProductId());
        model.addAttribute(orderLine);
        providerOrderService.findById(providerOrderId)
                .map(orderDto -> model.addAttribute("order", orderDto))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("orderlines", providerOrderLineService.findAllByProviderOrderId(providerOrderId));
        if (productNull) {
            model.addAttribute("products", productService.findPart());
            return "provider_order_line/provider_order_line_add";
        } else {
            productService.findByIdCustom(orderLine.getProductId())
                    .map(productDto -> model.addAttribute("product", productDto))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            model.addAttribute("productUnits", productUnitService.findAllByProductId(orderLine.getProductId()));
            model.addAttribute("qualities", qualityService.findAll());
            return "provider_order_line/provider_order_line_add2";
        }
    }

    @PostMapping
    public String create(@ModelAttribute @Validated ProviderOrderLineEditDto orderLine,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("orderLine", orderLine);
            redirectAttributes.addFlashAttribute("errorsLine", bindingResult.getAllErrors());
            return "redirect:/providerorderlines/" + orderLine.getProviderOrderId() + "/add";
        }
        providerOrderLineService.create(orderLine);
        return "redirect:/providerorders/" + orderLine.getProviderOrderId();
    }

    @GetMapping("/{providerOrderId}/{id}/delete")
    public String delete(@PathVariable("providerOrderId") Integer provideroOrderId, @PathVariable("id") Integer id) {
        if (providerOrderLineService.delete(id))
            return "redirect:/providerorders/" + provideroOrderId;
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
