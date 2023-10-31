package com.waynesouza.soccer.service.impl;

import com.waynesouza.soccer.domain.Partida;
import com.waynesouza.soccer.repository.PartidaRepository;
import com.waynesouza.soccer.service.PartidaService;
import com.waynesouza.soccer.service.dto.PartidaDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class PartidaServiceImpl implements PartidaService {

    private final ModelMapper mapper;
    private final PartidaRepository repository;

    @Override
    public PartidaDTO salvar(PartidaDTO dto) {
        if (verificarRegras(dto)) {
            throw new RuntimeException();
        }
        Partida partida = mapper.map(dto, Partida.class);
        return mapper.map(repository.save(partida), PartidaDTO.class);
    }

    private Boolean verificarRegras(PartidaDTO dto) {
        return verificarHorario(dto.getHorario()) && verificarDisponibilidadeEstadio(dto.getEstadio(), dto.getData()) &&
                verificarIntervaloEntrePartidas(dto.getTimeMandante(), dto.getTimeVisitante(), dto.getData(), dto.getHorario()) &&
                verificarHorarioNoFuturo(dto.getData(), dto.getHorario());
    }

    private Boolean verificarHorario(LocalTime horario) {
        return horario.isAfter(LocalTime.of(22, 0)) || horario.isBefore(LocalTime.of(8, 0));
    }

    private Boolean verificarDisponibilidadeEstadio(String estadio, LocalDate data) {
        return repository.countPartidasByEstadioAndData(estadio, data) <= 0;
    }

    private Boolean verificarIntervaloEntrePartidas(String timeMandante, String timeVisitante, LocalDate data, LocalTime horario) {
        return repository.countPartidasByTimesAndData(timeMandante, timeVisitante, data, horario) == 0;
    }

    private Boolean verificarHorarioNoFuturo(LocalDate data, LocalTime horario) {
        return LocalDateTime.of(data, horario).isAfter(LocalDateTime.now());
    }

}
