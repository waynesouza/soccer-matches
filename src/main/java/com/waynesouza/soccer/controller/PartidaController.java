package com.waynesouza.soccer.controller;

import com.waynesouza.soccer.service.PartidaService;
import com.waynesouza.soccer.dto.PartidaAtualizadaDTO;
import com.waynesouza.soccer.dto.PartidaDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PartidaController {

    private final PartidaService service;

    @PostMapping
    public ResponseEntity<PartidaDTO> criar(@Valid @RequestBody PartidaDTO dto) {
        log.info("Requisição para criar uma partida");
        return ResponseEntity.ok(service.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartidaDTO> atualizar(@PathVariable("id") String id, @RequestBody PartidaAtualizadaDTO dto) {
        log.info("Requisição para atualizar os dados de uma partida");
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<PartidaDTO>> listarTodas() {
        log.info("Requisição para listar todas as partidas");
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/goleadas")
    public ResponseEntity<List<PartidaDTO>> listarGoleadas() {
        log.info("Requisição para listar as partidas que terminaram em goleadas");
        return ResponseEntity.ok(service.listarGoleadas());
    }

    @GetMapping("/sem-gols")
    public ResponseEntity<List<PartidaDTO>> listarPartidasSemGols() {
        log.info("Requisição para listar as partidas que terminaram zero a zero");
        return ResponseEntity.ok(service.listarPartidasSemGols());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") String id) {
        log.info("Requisição para excluir uma partida");
        service.excluir(id);
        return ResponseEntity.ok().build();
    }

}
