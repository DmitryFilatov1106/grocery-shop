package ru.fildv.groceryshop.http.dto.user;

import lombok.Value;

@Value
public class UserFilterDto {
    String username;
    String firstname;
    String lastname;
    String role;
}
