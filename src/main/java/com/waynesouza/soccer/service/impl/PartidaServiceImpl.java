package com.waynesouza.soccer.service.impl;

import com.waynesouza.soccer.config.exception.ParametrizedMessageException;
import com.waynesouza.soccer.domain.Partida;
import com.waynesouza.soccer.repository.PartidaRepository;
import com.waynesouza.soccer.service.PartidaService;
import com.waynesouza.soccer.service.dto.PartidaAtualizadaDTO;
import com.waynesouza.soccer.service.dto.PartidaDTO;
import com.waynesouza.soccer.service.util.VerificacaoBase;
import com.waynesouza.soccer.util.ConstantesUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartidaServiceImpl implements PartidaService, VerificacaoBase {

    private final ModelMapper mapper;
    private final PartidaRepository repository;

    @Override
    public PartidaDTO criar(PartidaDTO dto) {
        verificarRegras(dto);
        Partida partida = mapper.map(dto, Partida.class);
        return mapper.map(repository.save(partida), PartidaDTO.class);
    }

    @Override
    public PartidaDTO atualizar(UUID id, PartidaAtualizadaDTO partidaAtualizadaDTO) {
        PartidaDTO partidaDTO = buscarPeloId(id);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(partidaAtualizadaDTO, partidaDTO);
        verificarRegras(partidaDTO);
        Partida partida = mapper.map(partidaDTO, Partida.class);
        return mapper.map(repository.save(partida), PartidaDTO.class);
    }

    private PartidaDTO buscarPeloId(UUID id) {
        Partida partida = repository.findById(id)
                .orElseThrow(() -> new ParametrizedMessageException(ConstantesUtil.ERRO_PARTIDA_NAO_ENCONTRADA));
        return mapper.map(partida, PartidaDTO.class);
    }

    private void verificarRegras(PartidaDTO dto) {
        Map<String, Boolean> regras = new HashMap<>();
        regras.put(ConstantesUtil.ERRO_HORARIO_PARTIDA, verificarHorario(dto.getHorario()));
        regras.put(ConstantesUtil.ERRO_DISPONIBILIDADE, verificarDisponibilidade(dto));
        regras.put(ConstantesUtil.ERRO_PARTIDA_FUTURO, verificarHorarioNoFuturo(dto.getData(), dto.getHorario()));
        regras.forEach((mensagem, condicao) -> verificarRegra(condicao, mensagem));
    }

    private Boolean verificarHorario(LocalTime horario) {
        return horario.isAfter(LocalTime.of(22, 0)) || horario.isBefore(LocalTime.of(8, 0));
    }

    private Boolean verificarDisponibilidade(PartidaDTO dto) {
        LocalDate dataLimiteInferior = dto.getData().minusDays(2);
        LocalDate dataLimiteSuperior = dto.getData().plusDays(2);
        return repository.verificarDisponibilidade(dto, dataLimiteInferior, dataLimiteSuperior);
    }

    private Boolean verificarHorarioNoFuturo(LocalDate data, LocalTime horario) {
        return LocalDateTime.of(data, horario).isAfter(LocalDateTime.now());
    }

}
