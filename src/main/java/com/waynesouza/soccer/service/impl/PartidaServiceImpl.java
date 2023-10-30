package com.waynesouza.soccer.service.impl;

import com.waynesouza.soccer.domain.Partida;
import com.waynesouza.soccer.repository.PartidaRepository;
import com.waynesouza.soccer.service.PartidaService;
import com.waynesouza.soccer.service.dto.PartidaDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class PartidaServiceImpl implements PartidaService {

    private final ModelMapper mapper;
    private final PartidaRepository repository;

    @Override
    public PartidaDTO salvar(PartidaDTO dto) throws Exception {
        if (!verificarRegras(dto)) {
            throw new Exception("Falha ao salvar!");
        }
        Partida partida = mapper.map(dto, Partida.class);
        return mapper.map(repository.save(partida), PartidaDTO.class);
    }

    private Boolean verificarRegras(PartidaDTO dto) {
        LocalTime horario = dto.getHorario();
        if (horario.isBefore(LocalTime.of(8, 0)) && horario.isAfter(LocalTime.of(22, 0))) {
            return false;
        } else if (repository.countPartidasByEstadioAndData(dto.getEstadio(), dto.getData()) > 0) {
            return false;
        // TODO regra para mais de uma partida de um mesmo clube com menos de dois dias de intervalo
        } else return !LocalDateTime.of(dto.getData(), dto.getHorario()).isAfter(LocalDateTime.now());
    }


}
