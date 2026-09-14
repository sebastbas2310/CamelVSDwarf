package com.example.camelvsdwarf.user.controller;

import com.example.camelvsdwarf.config.SecurityConfig;
import com.example.camelvsdwarf.user.Role;
import com.example.camelvsdwarf.user.UserRequest;
import com.example.camelvsdwarf.user.UserResponse;
import com.example.camelvsdwarf.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    @WithMockUser(username = "admin")
    void getUserById_shouldReturnOk() throws Exception {
        UserResponse response = new UserResponse(1L, "Ana García", "ana@test.com", Role.ADMINISTRATOR, true, LocalDateTime.now());
        when(userService.getUserById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Ana García"));
    }

    @Test
    @WithMockUser(username = "admin")
    void createUser_shouldReturnCreated() throws Exception {
        UserRequest request = new UserRequest("Luis Pérez", "luis@test.com", "Secreto123", Role.VIEWER);
        UserResponse response = new UserResponse(7L, "Luis Pérez", "luis@test.com", Role.VIEWER, true, LocalDateTime.now());
        when(userService.createUser(any(UserRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/users/7"))
                .andExpect(jsonPath("$.email").value("luis@test.com"));
    }
}
