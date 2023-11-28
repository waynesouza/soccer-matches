package com.waynesouza.soccer.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.waynesouza.soccer.domain.dto.PartidaAtualizadaDTO;
import com.waynesouza.soccer.domain.dto.PartidaDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PartidaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());


    @Test
    public void sucessoAoCriarPartidaTest() throws Exception {
        PartidaDTO request = criarPartidaDtoRequest();
        String data = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/api/partida")
                .contentType(MediaType.APPLICATION_JSON)
                .content(data))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        PartidaDTO dto = objectMapper.readValue(response, PartidaDTO.class);

        assertEquals(dto.getTimeMandante(), request.getTimeMandante());
        assertEquals(dto.getTimeVisitante(), request.getTimeVisitante());
        assertEquals(dto.getQuantidadeGolMandante(), request.getQuantidadeGolMandante());
        assertEquals(dto.getQuantidadeGolVisitante(), request.getQuantidadeGolVisitante());
        assertEquals(dto.getDataHora(), request.getDataHora());
        assertEquals(dto.getEstadio(), request.getEstadio());
    }

    @Test
    public void sucessoAoAtualizarPartidaTest() throws Exception {
        PartidaDTO dtoPost = criarPartida(criarPartidaDtoRequest());

        PartidaAtualizadaDTO request = criarPartidaAtualizadaDtoRequest();
        String data = objectMapper.writeValueAsString(request);

        MvcResult resultPut = mockMvc.perform(put("/api/partida/" + dtoPost.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andExpect(status().isOk())
                .andReturn();

        String responsePut = resultPut.getResponse().getContentAsString(StandardCharsets.UTF_8);
        PartidaDTO dtoPut = objectMapper.readValue(responsePut, PartidaDTO.class);

        assertEquals(dtoPut.getTimeMandante(), dtoPost.getTimeMandante());
        assertEquals(dtoPut.getTimeVisitante(), dtoPost.getTimeVisitante());
        assertEquals(dtoPut.getQuantidadeGolMandante(), dtoPost.getQuantidadeGolMandante());
        assertEquals(dtoPut.getQuantidadeGolVisitante(), dtoPost.getQuantidadeGolVisitante());
        assertEquals(dtoPut.getDataHora(), dtoPost.getDataHora());
        assertNotEquals(dtoPut.getEstadio(), dtoPost.getEstadio());
        assertEquals(dtoPut.getEstadio(), request.getEstadio());
    }

    @Test
    public void sucessoAoListarTodasPartidas() throws Exception {
        PartidaDTO request = criarPartidaDtoRequest();
        criarPartida(request);

        List<PartidaDTO> response = objectMapper
                .readValue(mockMvc.perform(get("/api/partida"))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
                });

        PartidaDTO dto = response.get(0);

        assertEquals(response.size(), 1);
        assertNotNull(dto.getId());
        assertEquals(dto.getTimeMandante(), request.getTimeMandante());
        assertEquals(dto.getTimeVisitante(), request.getTimeVisitante());
        assertEquals(dto.getQuantidadeGolMandante(), request.getQuantidadeGolMandante());
        assertEquals(dto.getQuantidadeGolVisitante(), request.getQuantidadeGolVisitante());
        assertEquals(dto.getDataHora(), request.getDataHora());
        assertEquals(dto.getEstadio(), request.getEstadio());
    }

    @SneakyThrows
    private PartidaDTO criarPartida(PartidaDTO request) {
        return objectMapper
                .readValue(mockMvc.perform(post("/api/partida")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
                });
    }

    private PartidaDTO criarPartidaDtoRequest() {
        PartidaDTO dto = new PartidaDTO();
        dto.setTimeMandante("FLU");
        dto.setTimeVisitante("FLA");
        dto.setQuantidadeGolMandante(BigDecimal.valueOf(4));
        dto.setQuantidadeGolVisitante(BigDecimal.ZERO);
        dto.setDataHora(LocalDateTime.now().minusDays(5));
        dto.setEstadio("Maracanã");
        return dto;
    }

    private PartidaAtualizadaDTO criarPartidaAtualizadaDtoRequest() {
        PartidaAtualizadaDTO dto = new PartidaAtualizadaDTO();
        dto.setEstadio("Outro");
        return dto;
    }

}
