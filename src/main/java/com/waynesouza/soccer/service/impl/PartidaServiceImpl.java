package com.waynesouza.soccer.service.impl;

import com.waynesouza.soccer.config.exception.ParametrizedMessageException;
import com.waynesouza.soccer.domain.Partida;
import com.waynesouza.soccer.domain.dto.FreguesDTO;
import com.waynesouza.soccer.domain.dto.RetrospectoClubeDTO;
import com.waynesouza.soccer.domain.dto.RetrospectoConfrontoDTO;
import com.waynesouza.soccer.repository.PartidaRepository;
import com.waynesouza.soccer.service.PartidaService;
import com.waynesouza.soccer.domain.dto.PartidaAtualizadaDTO;
import com.waynesouza.soccer.domain.dto.PartidaDTO;
import com.waynesouza.soccer.service.util.RegraBase;
import com.waynesouza.soccer.util.ConstantesUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.waynesouza.soccer.util.ConstantesUtil.MANDANTE;
import static com.waynesouza.soccer.util.ConstantesUtil.VISITANTE;

@Service
@RequiredArgsConstructor
public class PartidaServiceImpl implements PartidaService, RegraBase {

    private final ModelMapper mapper;
    private final PartidaRepository repository;

    @Override
    public PartidaDTO criar(PartidaDTO dto) {
        verificarRegras(dto);
        Partida partida = mapper.map(dto, Partida.class);
        return mapper.map(repository.save(partida), PartidaDTO.class);
    }

    @Override
    public PartidaDTO atualizar(String id, PartidaAtualizadaDTO partidaAtualizadaDTO) {
        PartidaDTO partidaDTO = mapper.map(buscarPeloId(id), PartidaDTO.class);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(partidaAtualizadaDTO, partidaDTO);
        verificarRegras(partidaDTO);
        Partida partida = mapper.map(partidaDTO, Partida.class);
        return mapper.map(repository.save(partida), PartidaDTO.class);
    }

    @Override
    public List<PartidaDTO> listarTodas() {
        return converterListaParaDTO(repository.findAll());
    }

    @Override
    public List<PartidaDTO> listarGoleadas() {
        return converterListaParaDTO(repository.listarGoleadas());
    }

    @Override
    public List<PartidaDTO> listarPartidasSemGols() {
        return converterListaParaDTO(repository.listarPartidasSemGols());
    }

    @Override
    public List<PartidaDTO> listarPartidasPorTime(String time, String filtro) {
        verificarRegra(Objects.nonNull(filtro) && !List.of(MANDANTE, VISITANTE).contains(filtro), ConstantesUtil.ERRO_VALOR_INVALIDO);
        return converterListaParaDTO(repository.listarPartidasPorTime(time, filtro));
    }

    @Override
    public List<PartidaDTO> listarPartidasPorEstadio(String estadio) {
        return converterListaParaDTO(repository.findAllByEstadio(estadio));
    }

    @Override
    public RetrospectoClubeDTO buscarRetrospectoTime(String time, String filtro) {
        return repository.buscarRetrospectoTime(time, filtro);
    }

    @Override
    public RetrospectoConfrontoDTO buscarRetrospectoConfronto(String primeiroTime, String segundoTime, String filtro) {
        return repository.buscarRetrospectoConfronto(primeiroTime, segundoTime, filtro);
    }

    @Override
    public List<FreguesDTO> listarFregueses(String time) {
        return repository.listarFregueses(time, PageRequest.of(0, 5));
    }

    @Override
    public void excluir(String id) {
        Partida partida = buscarPeloId(id);
        repository.delete(partida);
    }

    private Partida buscarPeloId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ParametrizedMessageException(ConstantesUtil.ERRO_PARTIDA_NAO_ENCONTRADA));
    }

    private void verificarRegras(PartidaDTO dto) {
        Map<String, Boolean> regras = new HashMap<>();
        regras.put(ConstantesUtil.ERRO_HORARIO_PARTIDA, verificarHorario(dto.getDataHora().toLocalTime()));
        regras.put(ConstantesUtil.ERRO_DISPONIBILIDADE_ESTADIO, verificarEstadio(dto));
        regras.put(ConstantesUtil.ERRO_DISPONIBILIDADE_PARTIDA, verificarDisponibilidade(dto));
        regras.put(ConstantesUtil.ERRO_PARTIDA_FUTURO, verificarHorarioNoFuturo(dto.getDataHora()));
        regras.forEach((mensagem, condicao) -> verificarRegra(condicao, mensagem));
    }

    private Boolean verificarHorario(LocalTime horario) {
        return horario.isAfter(LocalTime.of(22, 0)) || horario.isBefore(LocalTime.of(8, 0));
    }

    private Boolean verificarEstadio(PartidaDTO dto) {
        return repository.verificarEstadio(dto.getEstadio(), dto.getDataHora().toLocalDate());
    }

    private Boolean verificarDisponibilidade(PartidaDTO dto) {
        LocalDateTime dataLimiteInferior = dto.getDataHora().minusDays(2);
        LocalDateTime dataLimiteSuperior = dto.getDataHora().plusDays(2);
        return repository.verificarDisponibilidade(dto, dataLimiteInferior, dataLimiteSuperior);
    }

    private Boolean verificarHorarioNoFuturo(LocalDateTime dataHora) {
        return dataHora.isAfter(LocalDateTime.now());
    }

    private List<PartidaDTO> converterListaParaDTO(List<Partida> partidas) {
        return partidas.stream().map(partida -> mapper.map(partida, PartidaDTO.class)).collect(Collectors.toList());
    }

}
