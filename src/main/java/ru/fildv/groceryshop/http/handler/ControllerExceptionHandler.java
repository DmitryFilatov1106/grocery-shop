package ru.fildv.groceryshop.http.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice(basePackages = "ru.fildv.groceryshop.http.controller")
public class ControllerExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleExceptions(DataIntegrityViolationException e, Model model){
        model.addAttribute("error", "This table entry is referenced in other tables.");
        return "error/error403";
    }


    @ExceptionHandler(Exception.class)
    public String handleExceptions(Exception exception, Model model) {
        log.error("Failed to return response: " + exception.getClass(), exception);
        if (exception.getClass() == AccessDeniedException.class) {
            model.addAttribute("error", exception.getMessage());
            return "error/error403";
        }
        return "error/error500";
    }
}
