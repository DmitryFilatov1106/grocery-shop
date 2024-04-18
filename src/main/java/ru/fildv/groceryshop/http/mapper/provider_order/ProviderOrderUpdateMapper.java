package ru.fildv.groceryshop.http.mapper.provider_order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.ProviderOrder;
import ru.fildv.groceryshop.http.dto.provider_order.ProviderOrderEditDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ProviderOrderUpdateMapper implements Mapper<ProviderOrderEditDto, ProviderOrder> {

    @Override
    public ProviderOrder map(ProviderOrderEditDto from, ProviderOrder to) {
        copy(from, to);
        return to;
    }

    @Override
    public ProviderOrder map(ProviderOrderEditDto from) {
        ProviderOrder order = new ProviderOrder();
        copy(from, order);
        return order;
    }

    private void copy(ProviderOrderEditDto from, ProviderOrder to) {
        to.setOrderDate(from.getOrderDate());
        to.setComment(from.getComment());
        to.setCommit(Objects.isNull(from.getCommit()) ? false : from.getCommit());
    }
}
