package com.waynesouza.soccer.service;

import com.waynesouza.soccer.domain.dto.FreguesDTO;
import com.waynesouza.soccer.domain.dto.PartidaAtualizadaDTO;
import com.waynesouza.soccer.domain.dto.PartidaDTO;
import com.waynesouza.soccer.domain.dto.RetrospectoClubeDTO;
import com.waynesouza.soccer.domain.dto.RetrospectoConfrontoDTO;

import java.util.List;

public interface PartidaService {

    PartidaDTO criar(PartidaDTO dto);

    PartidaDTO atualizar(String id, PartidaAtualizadaDTO dto);

    List<PartidaDTO> listarTodas();

    List<PartidaDTO> listarGoleadas();

    List<PartidaDTO> listarPartidasSemGols();

    List<PartidaDTO> listarPartidasPorEquipe(String time, String filtro);

    List<PartidaDTO> listarPartidasPorEstadio(String estadio);

    RetrospectoClubeDTO buscarRetrospectoPorEquipe(String equipe, String filtro);

    RetrospectoConfrontoDTO buscarRetrospectoConfronto(String primeiraEquipe, String segundaEquipe, String filtro);

    List<FreguesDTO> listarFregueses(String time);

    void excluir(String id);

}
