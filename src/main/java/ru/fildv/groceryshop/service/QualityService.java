package ru.fildv.groceryshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fildv.groceryshop.database.repository.QualityRepository;
import ru.fildv.groceryshop.http.dto.quality.QualityEditDto;
import ru.fildv.groceryshop.http.dto.quality.QualityFilterDto;
import ru.fildv.groceryshop.http.dto.quality.QualityReadDto;
import ru.fildv.groceryshop.http.mapper.quality.QualityReadMapper;
import ru.fildv.groceryshop.http.mapper.quality.QualityUpdateMapper;

import java.util.*;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class QualityService {
    private final QualityRepository qualityRepository;
    private final QualityReadMapper qualityReadMapper;
    private final QualityUpdateMapper qualityUpdateMapper;

    @Value("${app.page.size}")
    private int sizePage;

    public Page<QualityReadDto> findAll(QualityFilterDto qualityFilterDto, int page) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        if (Objects.isNull(qualityFilterDto.getName()))
            return qualityRepository.findAll(PageRequest.of(page, sizePage, sort))
                    .map(qualityReadMapper::map);

        return qualityRepository.findAllByNameContainingIgnoreCase(qualityFilterDto.getName(), PageRequest.of(page, sizePage, sort))
                .map(qualityReadMapper::map);
    }

    public List<QualityReadDto> findAll() {
        return qualityRepository.findAll().stream()
                .map(qualityReadMapper::map)
                .sorted(Comparator.comparing(QualityReadDto::getName))
                .collect(toList());
    }

    public List<QualityReadDto> findAllFilter() {
        List<QualityReadDto> filter = new ArrayList<>();
        filter.add(new QualityReadDto(null, ""));
        filter.addAll(qualityRepository.findAll().stream()
                .map(qualityReadMapper::map)
                .sorted(Comparator.comparing(QualityReadDto::getName))
                .collect(toList()));
        return filter;

    }

    public Optional<QualityReadDto> findById(Integer id) {
        return qualityRepository.findById(id)
                .map(qualityReadMapper::map);
    }

    @Transactional
    public Optional<QualityReadDto> update(Integer id, QualityEditDto qualityDto) {
        return qualityRepository.findById(id)
                .map(entity -> qualityUpdateMapper.map(qualityDto, entity))
                .map(qualityRepository::saveAndFlush)
                .map(qualityReadMapper::map);
    }

    @Transactional
    public boolean delete(Integer id) {
        return qualityRepository.findById(id)
                .map(entity -> {
                    qualityRepository.delete(entity);
                    qualityRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public QualityReadDto create(QualityEditDto qualityDto) {
        return Optional.of(qualityDto)
                .map(qualityUpdateMapper::map)
                .map(qualityRepository::save)
                .map(qualityReadMapper::map)
                .orElseThrow();
    }
}
