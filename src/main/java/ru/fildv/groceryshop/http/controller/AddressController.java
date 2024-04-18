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
import ru.fildv.groceryshop.database.entity.enums.AddressFor;
import ru.fildv.groceryshop.http.dto.address.AddressEditDto;
import ru.fildv.groceryshop.service.AddressService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/addresses")
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/{id}/{addressfor}")
    public String findUserAddressById(@PathVariable Integer id,
                                      @PathVariable("addressfor") String addressfor,
                                      Model model) {
        var af = AddressFor.valueOf(addressfor.toUpperCase());
        return addressService.findById(id, af)
                .map(address -> {
                    model.addAttribute("id", id);
                    model.addAttribute("addressFor", addressfor);
                    model.addAttribute("description", address.getDescription());
                    model.addAttribute("region", address.getRegion());
                    model.addAttribute("city", address.getCity());
                    model.addAttribute("street", address.getStreet());
                    model.addAttribute("house", address.getHouse());
                    model.addAttribute("postalCode", address.getPostalCode());
                    return "address/address";
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/{addressfor}")
    public String update(@PathVariable Integer id,
                         @PathVariable String addressfor,
                         @ModelAttribute @Validated AddressEditDto address,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("id", id);
            redirectAttributes.addFlashAttribute("addressFor", addressfor);
            redirectAttributes.addFlashAttribute("description", address.getDescription());
            redirectAttributes.addFlashAttribute("region", address.getRegion());
            redirectAttributes.addFlashAttribute("city", address.getCity());
            redirectAttributes.addFlashAttribute("street", address.getStreet());
            redirectAttributes.addFlashAttribute("house", address.getHouse());
            redirectAttributes.addFlashAttribute("postalCode", address.getPostalCode());
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/addresses/" + id + "/" + addressfor;
        }
        if (addressService.update(id, AddressFor.valueOf(addressfor.toUpperCase()), address)) {
            String path = null;
            switch (addressfor) {
                case "user" -> path = "redirect:/users/" + id;
                case "customer" -> path = "redirect:/customers/" + id;
                default -> new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            return path;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
