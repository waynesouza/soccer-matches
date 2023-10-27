package com.waynesouza.soccer.service.impl;

import com.waynesouza.soccer.domain.Partida;
import com.waynesouza.soccer.repository.PartidaRepository;
import com.waynesouza.soccer.service.EstadioService;
import com.waynesouza.soccer.service.PartidaService;
import com.waynesouza.soccer.service.TimeService;
import com.waynesouza.soccer.service.dto.PartidaDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartidaServiceImpl implements PartidaService {

    private final ModelMapper mapper;
    private final PartidaRepository repository;
    private final TimeService timeService;
    private final EstadioService estadioService;

    @Override
    public PartidaDTO salvar(PartidaDTO partidaDTO) {
        Partida partida = mapper.map(partidaDTO, Partida.class);
        partida.setTimeMandante(timeService.buscarOuSalvar(partidaDTO.getTimeMandante()));
        partida.setTimeVisitante(timeService.buscarOuSalvar(partidaDTO.getTimeVisitante()));
        partida.setEstadio(estadioService.buscarOuSalvar(partidaDTO.getEstadio()));
        return mapper.map(repository.save(partida), PartidaDTO.class);
    }

}
