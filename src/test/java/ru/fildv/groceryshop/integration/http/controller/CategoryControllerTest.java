package ru.fildv.groceryshop.integration.http.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import ru.fildv.groceryshop.database.entity.enums.Role;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Disabled
@RequiredArgsConstructor
@AutoConfigureMockMvc
class CategoryControllerTest{//} extends IntegrationTestBase {
    private final MockMvc mockMvc;

    @BeforeEach
    void init() {
        List<GrantedAuthority> roles = Arrays.asList(Role.ADMIN, Role.USER, Role.MANAGER, Role.PROJECT_MANAGER, Role.STOREKEEPER);
        User testUser = new User("test@gmail.com", "test", roles);
        TestingAuthenticationToken token = new TestingAuthenticationToken(testUser, testUser.getPassword(), roles);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(token);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        view().name("category/categories")
                );
    }

}