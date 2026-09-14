package com.example.camelvsdwarf.team.controller;

import com.example.camelvsdwarf.config.SecurityConfig;
import com.example.camelvsdwarf.team.TeamRequest;
import com.example.camelvsdwarf.team.TeamResponse;
import com.example.camelvsdwarf.team.TeamStatus;
import com.example.camelvsdwarf.team.service.TeamService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeamController.class)
@Import(SecurityConfig.class)
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TeamService teamService;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    @WithMockUser(username = "admin")
    void getTeamById_shouldReturnOk() throws Exception {
        TeamResponse response = new TeamResponse(2L, "Atlas", "Equipo de prueba", "Coach Luna", TeamStatus.ACTIVE, 6, LocalDateTime.now(), 10, 2, List.of());
        when(teamService.getTeamById(2L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/teams/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Atlas"));
    }

    @Test
    @WithMockUser(username = "admin")
    void createTeam_shouldReturnCreated() throws Exception {
        TeamRequest request = new TeamRequest("Atlas", "Equipo de prueba", "Coach Luna", 6);
        TeamResponse response = new TeamResponse(9L, "Atlas", "Equipo de prueba", "Coach Luna", TeamStatus.ACTIVE, 6, LocalDateTime.now(), 0, 0, List.of());
        when(teamService.createTeam(any(TeamRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/teams")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/teams/9"))
                .andExpect(jsonPath("$.coach").value("Coach Luna"));
    }

    @Test
    @WithMockUser(username = "admin")
    void addCompetitorToTeam_shouldReturnCreated() throws Exception {
        mockMvc.perform(post("/api/v1/teams/3/competitors/44")
                        .with(csrf()))
                .andExpect(status().isCreated());
    }
}
