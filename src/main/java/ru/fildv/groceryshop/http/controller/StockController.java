package ru.fildv.groceryshop.http.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fildv.groceryshop.http.dto.page.PageResponseDto;
import ru.fildv.groceryshop.http.dto.stock.StockFilterDto;
import ru.fildv.groceryshop.http.dto.stock.StockSimpleReadDto;
import ru.fildv.groceryshop.service.ProductService;
import ru.fildv.groceryshop.service.QualityService;
import ru.fildv.groceryshop.service.StockService;

import java.util.Objects;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Controller
@RequestMapping("/stock")
public class StockController {
    private final StockService stockService;
    private final ProductService productService;
    private final QualityService qualityService;

    @GetMapping
    public String findAll(@RequestParam(name = "page", required = false, defaultValue = "0") int pageNumber,
                          @ModelAttribute("stockFilterDto") StockFilterDto filterDto,
                          Model model) {

        Page<StockSimpleReadDto> page = stockService.findAll(filterDto, pageNumber);

        model.addAttribute("data", PageResponseDto.of(page));
        model.addAttribute("allPages", IntStream.range(0, page.getTotalPages()).toArray());
        model.addAttribute("mapping", "stock");
        model.addAttribute("products", productService.findAllFilter());
        model.addAttribute("qualities", qualityService.findAllFilter());
        model.addAttribute("filter", filterDto);

        if (Objects.isNull(filterDto.getProductId()) && Objects.isNull(filterDto.getQualityId()))
            model.addAttribute("parfilter", "");
        else {
            var parProductId = Objects.isNull(filterDto.getProductId()) ? "" : filterDto.getProductId();
            var parQualityId = Objects.isNull(filterDto.getQualityId()) ? "" : filterDto.getQualityId();
            model.addAttribute("parfilter", "&productId=" + parProductId + "&qualityId=" + parQualityId);
        }
        return "stock/stocks";
    }
}
