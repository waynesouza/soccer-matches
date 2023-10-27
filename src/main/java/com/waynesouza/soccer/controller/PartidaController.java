package com.waynesouza.soccer.controller;

import com.waynesouza.soccer.service.PartidaService;
import com.waynesouza.soccer.service.dto.PartidaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/partida")
@RequiredArgsConstructor
public class PartidaController {

    private final PartidaService partidaService;

    @PostMapping
    public ResponseEntity<PartidaDTO> salvar(@RequestBody PartidaDTO partidaDTO) {
        return new ResponseEntity<>(partidaService.salvar(partidaDTO), HttpStatus.CREATED);
    }

}
