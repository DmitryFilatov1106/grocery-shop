package ru.fildv.groceryshop.service;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fildv.groceryshop.database.repository.provider_order.ProviderOrderRepository;
import ru.fildv.groceryshop.http.dto.provider_order.ProviderOrderEditDto;
import ru.fildv.groceryshop.http.dto.provider_order.ProviderOrderFilterDto;
import ru.fildv.groceryshop.http.dto.provider_order.ProviderOrderReadDto;
import ru.fildv.groceryshop.http.mapper.provider_order.ProviderOrderEditMapper;
import ru.fildv.groceryshop.http.mapper.provider_order.ProviderOrderReadMapper;
import ru.fildv.groceryshop.http.mapper.provider_order.ProviderOrderUpdateMapper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProviderOrderService {
    private final ProviderOrderRepository providerOrderRepository;
    private final ProviderOrderReadMapper providerOrderReadMapper;
    private final ProviderOrderUpdateMapper providerOrderUpdateMapper;
    private final ProviderOrderEditMapper providerOrderEditMapper;
    private final ProviderOrderLineService providerOrderLineService;

    @Value("${app.page.size}")
    private int sizePage;

    public Page<ProviderOrderReadDto> findAll(ProviderOrderFilterDto filter, int page) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        return providerOrderRepository.findAllByFilter(filter, PageRequest.of(page, sizePage, sort))
                .map(providerOrderReadMapper::map);
    }

    @Transactional(rollbackFor = OptimisticLockException.class)
    public Optional<ProviderOrderReadDto> update(Integer id, ProviderOrderEditDto orderDto) {
        return providerOrderRepository.findById(id)
                .map(entity -> {
                    if (!entity.getCommit() && orderDto.getCommit()) {
                        providerOrderLineService.commitLines(orderDto.getId());
                    }
                    providerOrderUpdateMapper.map(orderDto, entity);
                    return entity;
                })
                .map(providerOrderRepository::saveAndFlush)
                .map(providerOrderReadMapper::map);
    }

    @Transactional
    public boolean delete(Integer id) {
        return providerOrderRepository.findById(id)
                .map(entity -> {
                    providerOrderRepository.delete(entity);
                    providerOrderRepository.flush();
                    return true;
                }).orElse(false);
    }

    public Optional<ProviderOrderReadDto> findById(Integer id) {
        return providerOrderRepository.findById(id)
                .map(providerOrderReadMapper::map);
    }

    @Transactional
    public ProviderOrderReadDto create(ProviderOrderEditDto providerOrderDto) {
        return Optional.of(providerOrderDto)
                .map(providerOrderUpdateMapper::map)
                .map(providerOrderRepository::saveAndFlush)
                .map(providerOrderReadMapper::map)
                .orElseThrow();
    }
}
