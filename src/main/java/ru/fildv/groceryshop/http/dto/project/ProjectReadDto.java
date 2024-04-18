package ru.fildv.groceryshop.http.dto.project;

import lombok.Value;
import ru.fildv.groceryshop.http.dto.promanager.ProManagerReadDto;

@Value
public class ProjectReadDto {
    Integer id;
    String name;
    ProManagerReadDto proManager;
}
