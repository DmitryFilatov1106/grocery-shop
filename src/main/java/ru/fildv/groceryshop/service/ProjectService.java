package ru.fildv.groceryshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fildv.groceryshop.database.repository.ProjectRepository;
import ru.fildv.groceryshop.http.dto.project.ProjectEditDto;
import ru.fildv.groceryshop.http.dto.project.ProjectFilterDto;
import ru.fildv.groceryshop.http.dto.project.ProjectReadDto;
import ru.fildv.groceryshop.http.mapper.project.ProjectEditMapper;
import ru.fildv.groceryshop.http.mapper.project.ProjectReadMapper;
import ru.fildv.groceryshop.http.mapper.project.ProjectUpdateMapper;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectReadMapper projectReadMapper;
    private final ProjectUpdateMapper projectUpdateMapper;
    private final ProjectEditMapper projectEditMapper;

    @Value("${app.page.size}")
    private int sizePage;

    public Page<ProjectReadDto> findAll(ProjectFilterDto projectFilterDto, int page) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        if (Objects.isNull(projectFilterDto.getName()))
            return projectRepository.findAll(PageRequest.of(page, sizePage, sort))
                    .map(projectReadMapper::map);

        return projectRepository.findAllByNameContainingIgnoreCase(projectFilterDto.getName(), PageRequest.of(page, sizePage, sort))
                .map(projectReadMapper::map);
    }

    public List<ProjectReadDto> findAll() {
        return projectRepository.findAll().stream()
                .map(projectReadMapper::map)
                .sorted(Comparator.comparing(ProjectReadDto::getName))
                .collect(toList());

    }

    public Optional<ProjectReadDto> findById(Integer id) {
        return projectRepository.findById(id)
                .map(projectReadMapper::map);
    }

    public Optional<ProjectReadDto> findByIdCustom(Integer id) {
        return projectRepository.findByIdCustom(id)
                .map(projectReadMapper::map);
    }

    @Transactional
    public Optional<ProjectReadDto> update(Integer id, ProjectEditDto projectDto) {
        return projectRepository.findByIdCustom(id)
                .map(entity -> projectUpdateMapper.map(projectDto, entity))
                .map(projectRepository::saveAndFlush)
                .map(projectReadMapper::map);
    }

    @Transactional
    public boolean delete(Integer id) {
        return projectRepository.findById(id)
                .map(entity -> {
                    projectRepository.delete(entity);
                    projectRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public ProjectReadDto create(ProjectEditDto projectDto) {
        return Optional.of(projectDto)
                .map(projectUpdateMapper::map)
                .map(projectRepository::save)
                .map(projectReadMapper::map)
                .orElseThrow();
    }
}
