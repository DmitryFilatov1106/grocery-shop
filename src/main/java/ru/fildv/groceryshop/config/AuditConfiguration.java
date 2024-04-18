package ru.fildv.groceryshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.envers.repository.config.EnableEnversRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import ru.fildv.groceryshop.GroceryShopApplication;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
@EnableEnversRepositories(basePackageClasses = GroceryShopApplication.class)
public class AuditConfiguration {
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                .map(auth -> (UserDetails) auth.getPrincipal())
                .map(UserDetails::getUsername);
    }
}
