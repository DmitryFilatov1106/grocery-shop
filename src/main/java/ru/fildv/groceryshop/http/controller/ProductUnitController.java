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
import ru.fildv.groceryshop.http.dto.product_unit.ProductUnitEditDto;
import ru.fildv.groceryshop.service.ProductService;
import ru.fildv.groceryshop.service.ProductUnitService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/productunits")
@PreAuthorize("hasAnyAuthority({'ADMIN', 'STOREKEEPER'})")
public class ProductUnitController {
    private final ProductUnitService productUnitService;
    private final ProductService productService;

    @GetMapping("/{productId}/add")
    public String create(@PathVariable("productId") Integer productId,
                         Model model,
                         @ModelAttribute("productUnit") ProductUnitEditDto productUnit) {
        model.addAttribute(productUnit);
        model.addAttribute("product", productService.findByIdCustom(productId).orElse(null));
        model.addAttribute("units", productUnitService.findFreeUnits(productId));
        return "product_unit/product_unit_add";
    }

    @PostMapping
    public String create(@ModelAttribute @Validated ProductUnitEditDto productUnit,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productUnit", productUnit);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/productunits/" + productUnit.getProductId() + "/add";
        }
        productUnitService.create(productUnit);
        return "redirect:/products/" + productUnit.getProductId();
    }

    @GetMapping("/{productId}/{id}/delete")
    public String delete(@PathVariable("productId") Integer productId, @PathVariable("id") Integer id) {
        if (productUnitService.delete(id))
            return "redirect:/products/" + productId;
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
