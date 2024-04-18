package ru.fildv.groceryshop.http.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.fildv.groceryshop.http.dto.product.ProductEditDto;
import ru.fildv.groceryshop.http.dto.product.ProductFilterDto;
import ru.fildv.groceryshop.http.dto.product.ProductReadDto;
import ru.fildv.groceryshop.http.dto.product.ProductSimpleReadDto;
import ru.fildv.groceryshop.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductRestController {
    private final ProductService productService;

    @GetMapping(value = "/{id}/image", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> findImage(@PathVariable Integer id) {
        return productService.findImage(id)
                .map(content -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .contentLength(content.length)
                        .body(content)
                ).orElseGet(ResponseEntity.notFound()::build);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductSimpleReadDto> findAll(int page) {
        ProductFilterDto filterDto = new ProductFilterDto(null, null);
        Page<ProductSimpleReadDto> result = productService.findAll(filterDto, page);
        return result.getContent();
    }

    @GetMapping("/{id}")
    public ProductReadDto findById(@PathVariable("id") Integer id) {
        return productService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductReadDto create(@Validated @RequestBody ProductEditDto product) {
        return productService.create(product);
    }

    @PutMapping("/{id}")
    public ProductReadDto update(@PathVariable("id") Integer id,
                                 @Validated @RequestBody ProductEditDto product) {
        return productService.update(id, product)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Integer id) {
        if (!productService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
