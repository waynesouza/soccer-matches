package com.waynesouza.soccer.service;

import com.waynesouza.soccer.service.dto.PartidaAtualizadaDTO;
import com.waynesouza.soccer.service.dto.PartidaDTO;

public interface PartidaService {

    PartidaDTO criar(PartidaDTO dto);

    PartidaDTO atualizar(String id, PartidaAtualizadaDTO dto);

}
