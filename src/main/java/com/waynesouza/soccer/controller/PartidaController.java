package com.waynesouza.soccer.controller;

import com.waynesouza.soccer.service.PartidaService;
import com.waynesouza.soccer.dto.PartidaAtualizadaDTO;
import com.waynesouza.soccer.dto.PartidaDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/partida")
@RequiredArgsConstructor
@Validated
public class PartidaController {

    private final PartidaService service;

    @PostMapping
    public ResponseEntity<PartidaDTO> criar(@Valid @RequestBody PartidaDTO dto) {
        return ResponseEntity.ok(service.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartidaDTO> atualizar(@PathVariable("id") String id, @RequestBody PartidaAtualizadaDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<PartidaDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") String id) {
        service.excluir(id);
        return ResponseEntity.ok().build();
    }

}
