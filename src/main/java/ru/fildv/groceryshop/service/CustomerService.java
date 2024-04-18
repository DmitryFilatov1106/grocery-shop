package ru.fildv.groceryshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fildv.groceryshop.database.repository.customer.CustomerRepository;
import ru.fildv.groceryshop.http.dto.address.AddressEditDto;
import ru.fildv.groceryshop.http.dto.address.AddressInfoDto;
import ru.fildv.groceryshop.http.dto.customer.CustomerEditDto;
import ru.fildv.groceryshop.http.dto.customer.CustomerFilterDto;
import ru.fildv.groceryshop.http.dto.customer.CustomerReadDto;
import ru.fildv.groceryshop.http.dto.customer.CustomerSimpleReadDto;
import ru.fildv.groceryshop.http.mapper.address.AddressUpdateMapper;
import ru.fildv.groceryshop.http.mapper.customer.CustomerReadMapper;
import ru.fildv.groceryshop.http.mapper.customer.CustomerSimpleReadMapper;
import ru.fildv.groceryshop.http.mapper.customer.CustomerUpdateMapper;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerReadMapper customerReadMapper;
    private final CustomerSimpleReadMapper customerSimpleReadMapper;
    private final CustomerUpdateMapper customerUpdateMapper;
    private final AddressUpdateMapper addressUpdateMapper;

    @Value("${app.page.size}")
    private int sizePage;

    public Page<CustomerSimpleReadDto> findAll(CustomerFilterDto filter, int page) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        return customerRepository.findAllByFilter(filter, PageRequest.of(page, sizePage, sort))
                .map(customerSimpleReadMapper::map);
    }

    public List<CustomerReadDto> findAll() {
        return customerRepository.findAll().stream()
                .map(customerReadMapper::map)
                .collect(toList());
    }

    public Optional<CustomerReadDto> findById(Integer id) {
        return customerRepository.findById(id)
                .map(customerReadMapper::map);
    }

    public Optional<CustomerReadDto> findByIdCustom(Integer id) {
        return customerRepository.findByIdCustom(id)
                .map(customerReadMapper::map);
    }

    @Transactional
    public Optional<CustomerReadDto> update(Integer id, CustomerEditDto customerDto) {
        return customerRepository.findByIdCustom(id)
                .map(entity -> customerUpdateMapper.map(customerDto, entity))
                .map(customerRepository::saveAndFlush)
                .map(customerReadMapper::map);
    }

    @Transactional
    public boolean delete(Integer id) {
        return customerRepository.findById(id)
                .map(entity -> {
                    customerRepository.delete(entity);
                    customerRepository.flush();
                    return true;
                }).isPresent();
    }

    @Transactional
    public CustomerReadDto create(CustomerEditDto customerEditDto) {
        return Optional.of(customerEditDto)
                .map(customerUpdateMapper::map)
                .map(customerRepository::save)
                .map(customerReadMapper::map)
                .orElseThrow();
    }

    public Optional<AddressInfoDto> getAddress(Integer id) {
        return customerRepository.findAddressById(id);
    }

    public boolean setAddress(Integer id, AddressEditDto address) {
        return customerRepository.findById(id)
                .map(entity -> {
                    entity.setAddress(addressUpdateMapper.map(address));
                    return entity;
                })
                .map(customerRepository::saveAndFlush).isPresent();
    }
}
