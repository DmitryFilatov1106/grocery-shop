package ru.fildv.groceryshop.http.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.fildv.groceryshop.http.validation.impl.UserInfoValidator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = UserInfoValidator.class)
@Target(TYPE_USE)
@Retention(RUNTIME)
public @interface UserInfo {
    String message() default "Firstname or Lastname should be filled in";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
