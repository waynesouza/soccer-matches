package com.waynesouza.soccer.service;

import com.waynesouza.soccer.service.dto.PartidaAtualizadaDTO;
import com.waynesouza.soccer.service.dto.PartidaDTO;

import java.util.UUID;

public interface PartidaService {

    PartidaDTO criar(PartidaDTO dto);

    PartidaDTO atualizar(UUID id, PartidaAtualizadaDTO dto);

}
