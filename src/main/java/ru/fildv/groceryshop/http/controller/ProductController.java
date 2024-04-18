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
import ru.fildv.groceryshop.http.dto.product.ProductEditDto;
import ru.fildv.groceryshop.http.dto.product.ProductFilterDto;
import ru.fildv.groceryshop.http.dto.product.ProductSimpleReadDto;
import ru.fildv.groceryshop.service.CategoryService;
import ru.fildv.groceryshop.service.ProductService;
import ru.fildv.groceryshop.service.ProductUnitService;
import ru.fildv.groceryshop.service.UnitService;

import java.util.Objects;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final UnitService unitService;
    private final ProductUnitService productUnitService;

    @GetMapping
    public String findAll(@RequestParam(name = "page", required = false, defaultValue = "0") int pageNumber,
                          @ModelAttribute("productFilterDto") ProductFilterDto filterDto,
                          Model model) {
        Page<ProductSimpleReadDto> page = productService.findAll(filterDto, pageNumber);

        model.addAttribute("data", PageResponseDto.of(page));
        model.addAttribute("allPages", IntStream.range(0, page.getTotalPages()).toArray());
        model.addAttribute("mapping", "products");
        model.addAttribute("categories", categoryService.findAllFilter());
        model.addAttribute("filter", filterDto);

        if (Objects.isNull(filterDto.getName()) && Objects.isNull(filterDto.getCategoryId()))
            model.addAttribute("parfilter", "");
        else {
            var parName = filterDto.getName();
            var parCategoryId = Objects.isNull(filterDto.getCategoryId()) ? "" : filterDto.getCategoryId();
            model.addAttribute("parfilter", "&name=" + parName + "&categoryId=" + parCategoryId);
        }
        return "product/products";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Integer id, Model model) {
        return productService.findByIdCustom(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    model.addAttribute("comments", productService.getComments(product.getId()));
                    model.addAttribute("units", productUnitService.findAllByProductId(product.getId()));
                    model.addAttribute("categories", categoryService.findAll());
                    model.addAttribute("baseUnits", unitService.findAll());
                    return "product/product";
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Integer id,
                         @ModelAttribute @Validated ProductEditDto product,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("product", product);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/products/{id}";
        }
        return productService.update(id, product)
                .map(it -> "redirect:/products/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        if (productService.delete(id))
            return "redirect:/products";
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/add")
    public String create(Model model, @ModelAttribute("product") ProductEditDto product) {
        model.addAttribute(product);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("baseUnits", unitService.findAll());
        return "product/product_add";
    }

    @PostMapping
    public String create(@ModelAttribute @Validated ProductEditDto product,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("product", product);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/products/add";
        }
        var productId = productService.create(product).getId();
        return "redirect:/products/" + productId;
    }
}
