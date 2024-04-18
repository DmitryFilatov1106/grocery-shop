package ru.fildv.groceryshop.http.mapper.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Project;
import ru.fildv.groceryshop.http.dto.project.ProjectReadDto;
import ru.fildv.groceryshop.http.dto.promanager.ProManagerReadDto;
import ru.fildv.groceryshop.http.mapper.Mapper;
import ru.fildv.groceryshop.http.mapper.promanager.ProManagerReadMapper;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProjectReadMapper implements Mapper<Project, ProjectReadDto> {
    private final ProManagerReadMapper proManagerReadMapper;

    @Override
    public ProjectReadDto map(Project from) {
        ProManagerReadDto proManager = Optional.ofNullable(from.getProManager())
                .map(proManagerReadMapper::map)
                .orElse(null);

        return new ProjectReadDto(
                from.getId(),
                from.getName(),
                proManager
        );
    }
}
