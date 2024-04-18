package ru.fildv.groceryshop.http.mapper.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Project;
import ru.fildv.groceryshop.database.entity.user.User;
import ru.fildv.groceryshop.http.dto.project.ProjectEditDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ProjectEditMapper implements Mapper<Project, ProjectEditDto> {
    @Override
    public ProjectEditDto map(Project from) {
        Integer promanagerId = Optional.ofNullable(from.getProManager())
                .map(User::getId)
                .orElse(null);

        return new ProjectEditDto(
                from.getId(),
                from.getName(),
                promanagerId
        );
    }
}
