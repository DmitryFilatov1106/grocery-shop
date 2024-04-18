package ru.fildv.groceryshop.http.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.fildv.groceryshop.http.dto.user.UserEditDto;
import ru.fildv.groceryshop.http.validation.UserInfo;

@Component
public class UserInfoValidator implements ConstraintValidator<UserInfo, UserEditDto> {
    @Override
    public boolean isValid(UserEditDto value, ConstraintValidatorContext context) {
        return StringUtils.hasText(value.getFirstname()) || StringUtils.hasText(value.getLastname());
    }
}
