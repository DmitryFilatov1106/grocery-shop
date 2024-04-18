package ru.fildv.groceryshop.database.repository.provider_order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.fildv.groceryshop.database.entity.ProviderOrder;
import ru.fildv.groceryshop.http.dto.provider_order.ProviderOrderFilterDto;

public interface ProviderOrderFilterRepository {
    Page<ProviderOrder> findAllByFilter(ProviderOrderFilterDto filter, Pageable pageable);
}
