package ru.fildv.groceryshop.database.repository.provider_order;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fildv.groceryshop.database.entity.ProviderOrder;

public interface ProviderOrderRepository extends JpaRepository<ProviderOrder, Integer>, ProviderOrderFilterRepository {
}
