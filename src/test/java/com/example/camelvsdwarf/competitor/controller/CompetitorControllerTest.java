package com.example.camelvsdwarf.competitor.controller;

import com.example.camelvsdwarf.competitor.CompetitorRequest;
import com.example.camelvsdwarf.competitor.CompetitorResponse;
import com.example.camelvsdwarf.competitor.CompetitorStatus;
import com.example.camelvsdwarf.competitor.CompetitorType;
import com.example.camelvsdwarf.competitor.service.CompetitorService;
import com.example.camelvsdwarf.config.SecurityConfig;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompetitorController.class)
@Import(SecurityConfig.class)
class CompetitorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CompetitorService competitorService;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    @WithMockUser(username = "admin")
    void getCompetitorById_shouldReturnOk() throws Exception {
        CompetitorResponse response = new CompetitorResponse(
                9L,
                "Mickey",
                "Miki",
                CompetitorType.DWARF,
                LocalDate.of(1997, 1, 15),
                new BigDecimal("58.50"),
                new BigDecimal("145.00"),
                "Málaga",
                CompetitorStatus.ACTIVE,
                LocalDateTime.now(),
                2,
                1,
                8
        );

        when(competitorService.getCompetitorById(9L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/competitors/9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(9))
                .andExpect(jsonPath("$.nickname").value("Miki"));
    }

    @Test
    @WithMockUser(username = "admin")
    void createCompetitor_shouldReturnCreated() throws Exception {
        CompetitorRequest request = new CompetitorRequest(
                "Mickey",
                "Miki",
                CompetitorType.DWARF,
                LocalDate.of(1997, 1, 15),
                new BigDecimal("58.50"),
                new BigDecimal("145.00"),
                "Málaga"
        );

        CompetitorResponse response = new CompetitorResponse(
                12L,
                "Mickey",
                "Miki",
                CompetitorType.DWARF,
                LocalDate.of(1997, 1, 15),
                new BigDecimal("58.50"),
                new BigDecimal("145.00"),
                "Málaga",
                CompetitorStatus.ACTIVE,
                LocalDateTime.now(),
                0,
                0,
                0
        );

        when(competitorService.createCompetitor(any(CompetitorRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/competitors")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/competitors/12"))
                .andExpect(jsonPath("$.nickname").value("Miki"));
    }
}
