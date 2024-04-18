package ru.fildv.groceryshop.http.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "ru.fildv.groceryshop.http.rest")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {
}
