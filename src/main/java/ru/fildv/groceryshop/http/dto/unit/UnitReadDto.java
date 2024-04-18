package ru.fildv.groceryshop.http.dto.unit;

import lombok.Value;

@Value
public class UnitReadDto {
    Integer id;
    String name;
    String shortName;
}
