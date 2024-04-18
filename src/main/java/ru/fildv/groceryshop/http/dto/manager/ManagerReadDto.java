package ru.fildv.groceryshop.http.dto.manager;

import lombok.Value;

@Value
public class ManagerReadDto {
    Integer id;
    String username;
    String firstname;
    String lastname;
}
