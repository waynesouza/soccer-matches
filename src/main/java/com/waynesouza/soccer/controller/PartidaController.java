package com.waynesouza.soccer.controller;

import com.waynesouza.soccer.domain.dto.FreguesDTO;
import com.waynesouza.soccer.domain.dto.RetrospectoClubeDTO;
import com.waynesouza.soccer.domain.dto.RetrospectoConfrontoDTO;
import com.waynesouza.soccer.service.PartidaService;
import com.waynesouza.soccer.domain.dto.PartidaAtualizadaDTO;
import com.waynesouza.soccer.domain.dto.PartidaDTO;
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
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<PartidaDTO> atualizar(@PathVariable("id") String id,
                                                @RequestBody PartidaAtualizadaDTO dto) {
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

    @GetMapping("/equipe/")
    public ResponseEntity<List<PartidaDTO>> listarPartidasPorTime(@RequestParam("equipe") String equipe,
                                                                  @RequestParam(name = "filtro", required = false) String filtro) {
        log.info("Requisição para listar as partidas de uma equipe específica");
        return ResponseEntity.ok(service.listarPartidasPorEquipe(equipe, filtro));
    }

    @GetMapping("/estadio/")
    public ResponseEntity<List<PartidaDTO>> listarPartidasPorEstadio(@RequestParam("estadio") String estadio) {
        log.info("Requisição para listar as partidas de um estádio específico");
        return ResponseEntity.ok(service.listarPartidasPorEstadio(estadio));
    }

    @GetMapping("/retrospecto-equipe/")
    public ResponseEntity<RetrospectoClubeDTO> buscarRetrospectoTime(@RequestParam("equipe") String equipe,
                                                                     @RequestParam(name = "filtro", required = false) String filtro) {
        log.info("Requisição para buscar o retrospecto das partidas de uma equipe específica");
        return ResponseEntity.ok(service.buscarRetrospectoPorEquipe(equipe, filtro));
    }

    @GetMapping("/retrospecto-confronto/")
    public ResponseEntity<RetrospectoConfrontoDTO> buscarRetrospectoConfronto(@RequestParam("primeiraEquipe") String primeiraEquipe,
                                                                              @RequestParam("segundaEquipe") String segundaEquipe,
                                                                              @RequestParam(name = "filtro", required = false) String filtro) {
        log.info("Requisição para buscar o retrospecto dos confrontos entre duas equipes específicas");
        return ResponseEntity.ok(service.buscarRetrospectoConfronto(primeiraEquipe, segundaEquipe, filtro));
    }

    @GetMapping("/fregueses/")
    public ResponseEntity<List<FreguesDTO>> listarFregueses(@RequestParam("equipe") String equipe) {
        log.info("Requisição para listar os fregueses de uma equipe específica");
        return ResponseEntity.ok(service.listarFregueses(equipe));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") String id) {
        log.info("Requisição para excluir uma partida");
        service.excluir(id);
        return ResponseEntity.ok().build();
    }

}
