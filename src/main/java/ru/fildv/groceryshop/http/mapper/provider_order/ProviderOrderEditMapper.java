package ru.fildv.groceryshop.http.mapper.provider_order;

import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.ProviderOrder;
import ru.fildv.groceryshop.http.dto.provider_order.ProviderOrderEditDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

@Component
public class ProviderOrderEditMapper implements Mapper<ProviderOrder, ProviderOrderEditDto> {
    @Override
    public ProviderOrderEditDto map(ProviderOrder from) {
        return new ProviderOrderEditDto(
                from.getId(),
                from.getOrderDate(),
                from.getComment(),
                from.getCommit()
        );
    }
}
