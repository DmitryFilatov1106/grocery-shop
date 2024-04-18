package ru.fildv.groceryshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fildv.groceryshop.database.repository.UnitRepository;
import ru.fildv.groceryshop.http.dto.unit.UnitChoiceDto;
import ru.fildv.groceryshop.http.dto.unit.UnitEditDto;
import ru.fildv.groceryshop.http.dto.unit.UnitFilterDto;
import ru.fildv.groceryshop.http.dto.unit.UnitReadDto;
import ru.fildv.groceryshop.http.mapper.unit.UnitChoiceMapper;
import ru.fildv.groceryshop.http.mapper.unit.UnitReadMapper;
import ru.fildv.groceryshop.http.mapper.unit.UnitUpdateMapper;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnitService {
    private final UnitRepository unitRepository;
    private final UnitReadMapper unitReadMapper;
    private final UnitChoiceMapper unitChoiceMapper;
    private final UnitUpdateMapper unitUpdateMapper;

    @Value("${app.page.size}")
    private int sizePage;

    public Page<UnitReadDto> findAll(UnitFilterDto unitFilterDto, int page) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        if (Objects.isNull(unitFilterDto.getName()))
            return unitRepository.findAll(PageRequest.of(page, sizePage, sort))
                    .map(unitReadMapper::map);

        return unitRepository.findAllByNameContainingIgnoreCase(unitFilterDto.getName(), PageRequest.of(page, sizePage, sort))
                .map(unitReadMapper::map);
    }

    public List<UnitChoiceDto> findAll() {
        return unitRepository.findAll().stream()
                .map(unitChoiceMapper::map)
                .sorted(Comparator.comparing(UnitChoiceDto::getShortName))
                .collect(toList());
    }

    public Optional<UnitReadDto> findById(Integer id) {
        return unitRepository.findById(id)
                .map(unitReadMapper::map);
    }

    @Transactional
    public Optional<UnitReadDto> update(Integer id, UnitEditDto unitDto) {
        return unitRepository.findById(id)
                .map(entity -> unitUpdateMapper.map(unitDto, entity))
                .map(unitRepository::saveAndFlush)
                .map(unitReadMapper::map);
    }

    @Transactional
    public boolean delete(Integer id) {
        return unitRepository.findById(id)
                .map(entity -> {
                    unitRepository.delete(entity);
                    unitRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public UnitReadDto create(UnitEditDto unitDto) {
        return Optional.of(unitDto)
                .map(unitUpdateMapper::map)
                .map(unitRepository::save)
                .map(unitReadMapper::map)
                .orElseThrow();
    }
}
