package ru.fildv.groceryshop.http.dto.page;

import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

@Value
public class PageResponseDto<T> {
    List<T> content;
    Metadata metadata;

    public static <T> PageResponseDto<T> of(Page<T> page) {
        var metadata = new Metadata(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
        return new PageResponseDto<>(page.getContent(), metadata);
    }

    @Value
    public static class Metadata {
        int page;
        int size;
        long totalElements;
        long totalPages;
    }
}
