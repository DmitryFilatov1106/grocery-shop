package ru.fildv.groceryshop.http.mapper.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Project;
import ru.fildv.groceryshop.database.entity.user.ProManager;
import ru.fildv.groceryshop.database.repository.user.ProManagerRepository;
import ru.fildv.groceryshop.http.dto.project.ProjectEditDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProjectUpdateMapper implements Mapper<ProjectEditDto, Project> {
    private final ProManagerRepository proManagerRepository;

    @Override
    public Project map(ProjectEditDto from, Project to) {
        copy(from, to);
        return to;
    }

    @Override
    public Project map(ProjectEditDto from) {
        Project project = new Project();
        copy(from, project);
        return project;
    }

    private void copy(ProjectEditDto from, Project to) {
        to.setName(from.getName());
        to.setProManager(getProManager(from.getPromanagerId()));
    }

    public ProManager getProManager(Integer proManagerId) {
        return Optional.ofNullable(proManagerId)
                .flatMap(proManagerRepository::findById)
                .orElse(null);
    }
}
