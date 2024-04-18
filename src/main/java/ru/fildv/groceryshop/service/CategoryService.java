package ru.fildv.groceryshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fildv.groceryshop.database.repository.CategoryRepository;
import ru.fildv.groceryshop.http.dto.category.CategoryEditDto;
import ru.fildv.groceryshop.http.dto.category.CategoryFilterDto;
import ru.fildv.groceryshop.http.dto.category.CategoryReadDto;
import ru.fildv.groceryshop.http.mapper.category.CategoryReadMapper;
import ru.fildv.groceryshop.http.mapper.category.CategoryUpdateMapper;

import java.util.*;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryReadMapper categoryReadMapper;
    private final CategoryUpdateMapper categoryUpdateMapper;

    @Value("${app.page.size}")
    private int sizePage;

    public Page<CategoryReadDto> findAll(CategoryFilterDto categoryFilterDto, int page) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        if (Objects.isNull(categoryFilterDto.getName()))
            return categoryRepository.findAll(PageRequest.of(page, sizePage, sort))
                    .map(categoryReadMapper::map);

        return categoryRepository.findAllByNameContainingIgnoreCase(categoryFilterDto.getName(), PageRequest.of(page, sizePage, sort))
                .map(categoryReadMapper::map);
    }

    public List<CategoryReadDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryReadMapper::map)
                .sorted(Comparator.comparing(CategoryReadDto::getName))
                .collect(toList());

    }

    public List<CategoryReadDto> findAllFilter() {
        List<CategoryReadDto> filter = new ArrayList<>();
        filter.add(new CategoryReadDto(null, ""));
        filter.addAll(categoryRepository.findAll().stream()
                .map(categoryReadMapper::map)
                .sorted(Comparator.comparing(CategoryReadDto::getName))
                .toList());
        return filter;

    }

    public Optional<CategoryReadDto> findById(Integer id) {
        return categoryRepository.findById(id)
                .map(categoryReadMapper::map);
    }

    @Transactional
    public Optional<CategoryReadDto> update(Integer id, CategoryEditDto categoryDto) {
        return categoryRepository.findById(id)
                .map(entity -> categoryUpdateMapper.map(categoryDto, entity))
                .map(categoryRepository::saveAndFlush)
                .map(categoryReadMapper::map);
    }

    @Transactional
    public boolean delete(Integer id) {
        return categoryRepository.findById(id)
                .map(entity -> {
                    categoryRepository.delete(entity);
                    categoryRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public CategoryReadDto create(CategoryEditDto categoryDto) {
        return Optional.of(categoryDto)
                .map(categoryUpdateMapper::map)
                .map(categoryRepository::save)
                .map(categoryReadMapper::map)
                .orElseThrow();
    }
}
