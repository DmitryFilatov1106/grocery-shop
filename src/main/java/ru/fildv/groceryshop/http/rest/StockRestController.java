package ru.fildv.groceryshop.http.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fildv.groceryshop.http.dto.stock.StockFilterDto;
import ru.fildv.groceryshop.http.dto.stock.StockSimpleReadDto;
import ru.fildv.groceryshop.service.StockService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/stock")
public class StockRestController {
    private final StockService stockService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StockSimpleReadDto> findAll(int page) {
        StockFilterDto filter = new StockFilterDto(null, null);
        Page<StockSimpleReadDto> result = stockService.findAll(filter, page);
        return result.getContent();
    }
}
