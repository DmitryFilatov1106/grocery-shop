package ru.fildv.groceryshop.service;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fildv.groceryshop.database.repository.customer_order.CustomerOrderRepository;
import ru.fildv.groceryshop.exception.StockEmpty;
import ru.fildv.groceryshop.http.dto.customer_order.CustomerOrderEditDto;
import ru.fildv.groceryshop.http.dto.customer_order.CustomerOrderFilterDto;
import ru.fildv.groceryshop.http.dto.customer_order.CustomerOrderReadDto;
import ru.fildv.groceryshop.http.dto.customer_order.CustomerOrderSimpleReadDto;
import ru.fildv.groceryshop.http.mapper.customer_order.CustomerOrderReadMapper;
import ru.fildv.groceryshop.http.mapper.customer_order.CustomerOrderSimpleReadMapper;
import ru.fildv.groceryshop.http.mapper.customer_order.CustomerOrderUpdateMapper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerOrderService {
    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerOrderReadMapper customerOrderReadMapper;
    private final CustomerOrderSimpleReadMapper customerOrderSimpleReadMapper;
    private final CustomerOrderUpdateMapper customerOrderUpdateMapper;
    private final CustomerOrderLineService customerOrderLineService;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${app.page.size}")
    private int sizePage;

    public Page<CustomerOrderSimpleReadDto> findAll(CustomerOrderFilterDto filter, int page) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        return customerOrderRepository.findAllByFilter(filter, PageRequest.of(page, sizePage, sort))
                .map(customerOrderSimpleReadMapper::map);
    }

    @Transactional(rollbackFor = {StockEmpty.class, OptimisticLockException.class})
    @PreAuthorize("hasAnyAuthority({'ADMIN', 'MANAGER'})")
    public Optional<CustomerOrderReadDto> update(Integer id, CustomerOrderEditDto orderDto) {
        return customerOrderRepository.findById(id)
                .map(entity -> {
                    if (!entity.getCommit() && orderDto.getCommit()) {
                        var result = customerOrderLineService.commitLines(orderDto.getId());
                        if (result.size() > 0) {
                            throw new StockEmpty(result);
                        }
                    }
                    customerOrderUpdateMapper.map(orderDto, entity);
                    return entity;
                })
                .map(customerOrderRepository::saveAndFlush)
                .map(customerOrderReadMapper::map);
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority({'ADMIN', 'MANAGER'})")
    public boolean delete(Integer id) {
        return customerOrderRepository.findById(id)
                .map(entity -> {
                    customerOrderRepository.delete(entity);
                    customerOrderRepository.flush();
                    return true;
                }).orElse(false);
    }

    public Optional<CustomerOrderReadDto> findById(Integer id) {
        return customerOrderRepository.findById(id)
                .map(customerOrderReadMapper::map);
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority({'ADMIN', 'MANAGER'})")
    public CustomerOrderReadDto create(CustomerOrderEditDto customerOrderDto) {
        return Optional.of(customerOrderDto)
                .map(customerOrderUpdateMapper::map)
                .map(customerOrderRepository::save)
                .map(customerOrderReadMapper::map)
                .orElseThrow();
    }

}
