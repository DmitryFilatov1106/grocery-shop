package ru.fildv.groceryshop.http.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.fildv.groceryshop.http.dto.comment.CommentEditDto;
import ru.fildv.groceryshop.service.ProductService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/comments")
public class CommentController {
    private final ProductService productService;

    @GetMapping("/{productId}/add")
    public String create(@PathVariable("productId") Integer productId,
                         Model model,
                         @ModelAttribute("comment") CommentEditDto comment) {
        model.addAttribute(comment);
        model.addAttribute("product", productService.findByIdCustom(productId).orElse(null));
        return "comment/comment_add";
    }

    @PostMapping
    public String create(@ModelAttribute @Validated CommentEditDto comment,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("comment", comment);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/comments/" + comment.getProductId() + "/add";
        }

        productService.addComment(comment);
        return "redirect:/products/" + comment.getProductId();
    }

    @GetMapping("/{productId}/{id}/delete")
    public String delete(@PathVariable("productId") Integer productId, @PathVariable("id") Integer id) {
        if (productService.deleteComment(productId, id))
            return "redirect:/products/" + productId;
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
