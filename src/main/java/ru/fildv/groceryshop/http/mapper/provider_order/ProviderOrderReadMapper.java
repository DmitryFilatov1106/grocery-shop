package ru.fildv.groceryshop.http.mapper.provider_order;

import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.ProviderOrder;
import ru.fildv.groceryshop.http.dto.provider_order.ProviderOrderReadDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

import java.time.format.DateTimeFormatter;

@Component
public class ProviderOrderReadMapper implements Mapper<ProviderOrder, ProviderOrderReadDto> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public ProviderOrderReadDto map(ProviderOrder from) {
        return new ProviderOrderReadDto(
                from.getId(),
                from.getOrderDate(),
                from.getOrderDate().format(formatter),
                from.getCommit(),
                from.getCommit() ? "v" : "-",
                from.getComment()
        );
    }
}