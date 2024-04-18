package ru.fildv.groceryshop.http.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shutdown")
@PreAuthorize("hasAuthority('ADMIN')")
public class ShutdownController {
    private static final int RETURN_CODE = 0;
    private final ConfigurableApplicationContext context;

    @GetMapping
    public void shutdown() {
        var user = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(auth -> (UserDetails) auth.getPrincipal())
                .map(UserDetails::getUsername);

        log.info("Shutdown by: [ " + user.orElse("") + " ]");
        System.exit(SpringApplication.exit(context, () -> RETURN_CODE));
    }
}
