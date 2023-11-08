package com.waynesouza.soccer.controller;

import com.waynesouza.soccer.service.PartidaService;
import com.waynesouza.soccer.service.dto.PartidaAtualizadaDTO;
import com.waynesouza.soccer.service.dto.PartidaDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/partida")
@RequiredArgsConstructor
@Validated
public class PartidaController {

    private final PartidaService partidaService;

    @PostMapping
    public ResponseEntity<PartidaDTO> criar(@Valid @RequestBody PartidaDTO dto) {
        return ResponseEntity.ok(partidaService.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartidaDTO> atualizar(@PathVariable("id") UUID id, @RequestBody PartidaAtualizadaDTO dto) {
        return ResponseEntity.ok(partidaService.atualizar(id, dto));
    }

}
