package ru.fildv.groceryshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static ru.fildv.groceryshop.database.entity.enums.Role.*;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //.csrf().disable()
                .authorizeHttpRequests(
                        configurer -> configurer
                                .requestMatchers(
                                        "/login",
                                        "/registration",
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/styles/**",
                                        "/api/v1/users/**",
                                        "/api/v1/products/**").permitAll()
                                .requestMatchers(
                                        "/changepassword",
                                        "/start",
                                        "/stock/**",
                                        "/addresses/**",
                                        "/comments/**",
                                        "/productunits/**").hasAnyAuthority(
                                        PROJECT_MANAGER.getAuthority(),
                                        MANAGER.getAuthority(),
                                        USER.getAuthority(),
                                        STOREKEEPER.getAuthority(),
                                        ADMIN.getAuthority()
                                )
                                .requestMatchers("/users/**",
                                        "/api/v1/shutdown").hasAuthority(ADMIN.getAuthority())
                                .requestMatchers("/customers/**").hasAnyAuthority(
                                        ADMIN.getAuthority(),
                                        MANAGER.getAuthority())
                                .requestMatchers("/projects/**").hasAnyAuthority(
                                        ADMIN.getAuthority(),
                                        PROJECT_MANAGER.getAuthority())
                                .requestMatchers("/categories/**").hasAnyAuthority(
                                        ADMIN.getAuthority(),
                                        MANAGER.getAuthority())
                                .requestMatchers("/units/**").hasAnyAuthority(
                                        ADMIN.getAuthority(),
                                        STOREKEEPER.getAuthority())
                                .requestMatchers("/qualities/**").hasAnyAuthority(
                                        ADMIN.getAuthority(),
                                        STOREKEEPER.getAuthority())
                                .requestMatchers("/products/**").hasAnyAuthority(
                                        ADMIN.getAuthority(),
                                        MANAGER.getAuthority(),
                                        STOREKEEPER.getAuthority()
                                )
                                .requestMatchers(
                                        "/providerorders/**",
                                        "/providerorderlines/**").hasAnyAuthority(
                                        ADMIN.getAuthority(),
                                        STOREKEEPER.getAuthority())
                                .requestMatchers(
                                        "/customerorders/**",
                                        "/customerorderlines/**").hasAnyAuthority(
                                        ADMIN.getAuthority(),
                                        MANAGER.getAuthority(),
                                        PROJECT_MANAGER.getAuthority()
                                )
                                .anyRequest().denyAll()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/start", true))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID"))
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
