package com.waynesouza.soccer.service;

import com.waynesouza.soccer.dto.PartidaAtualizadaDTO;
import com.waynesouza.soccer.dto.PartidaDTO;

import java.util.List;

public interface PartidaService {

    PartidaDTO criar(PartidaDTO dto);

    PartidaDTO atualizar(String id, PartidaAtualizadaDTO dto);

    List<PartidaDTO> listarTodas();

    void excluir(String id);

}
