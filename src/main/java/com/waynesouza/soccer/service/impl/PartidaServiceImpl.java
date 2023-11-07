package com.waynesouza.soccer.service.impl;

import com.waynesouza.soccer.domain.Partida;
import com.waynesouza.soccer.repository.PartidaRepository;
import com.waynesouza.soccer.service.PartidaService;
import com.waynesouza.soccer.service.dto.PartidaDTO;
import com.waynesouza.soccer.service.util.VerificacaoBase;
import com.waynesouza.soccer.util.ConstantesUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PartidaServiceImpl implements PartidaService, VerificacaoBase {

    private final ModelMapper mapper;
    private final PartidaRepository repository;

    @Override
    public PartidaDTO salvar(PartidaDTO dto) {
        verificarRegras(dto);
        Partida partida = mapper.map(dto, Partida.class);
        return mapper.map(repository.save(partida), PartidaDTO.class);
    }

    private void verificarRegras(PartidaDTO dto) {
        Map<String, Boolean> regras = new HashMap<>();
        regras.put(ConstantesUtil.ERRO_HORARIO_PARTIDA, verificarHorario(dto.getHorario()));
        regras.put(ConstantesUtil.ERRO_DISPONIBILIDADE_ESTADIO, verificarDisponibilidadeEstadio(dto.getEstadio(), dto.getData()));
        regras.put(ConstantesUtil.ERRO_INTERVALO_PARTIDAS, verificarIntervaloEntrePartidas(dto.getTimeMandante(), dto.getTimeVisitante(), dto.getData(), dto.getHorario()));
        regras.put(ConstantesUtil.ERRO_PARTIDA_FUTURO, verificarHorarioNoFuturo(dto.getData(), dto.getHorario()));
        regras.forEach((mensagem, condicao) -> verificarRegra(condicao, mensagem));
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
