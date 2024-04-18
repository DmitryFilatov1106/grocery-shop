package ru.fildv.groceryshop.http.dto.promanager;

import lombok.Value;

@Value
public class ProManagerReadDto {
    Integer id;
    String username;
    String firstname;
    String lastname;
}
