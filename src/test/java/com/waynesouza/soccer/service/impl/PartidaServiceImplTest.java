package com.waynesouza.soccer.service.impl;

import com.waynesouza.soccer.config.exception.ParametrizedMessageException;
import com.waynesouza.soccer.domain.Partida;
import com.waynesouza.soccer.domain.dto.FreguesDTO;
import com.waynesouza.soccer.domain.dto.PartidaAtualizadaDTO;
import com.waynesouza.soccer.domain.dto.PartidaDTO;
import com.waynesouza.soccer.domain.dto.RetrospectoClubeDTO;
import com.waynesouza.soccer.domain.dto.RetrospectoConfrontoDTO;
import com.waynesouza.soccer.repository.PartidaRepository;
import com.waynesouza.soccer.util.ConstantesUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PartidaServiceImplTest {

    @InjectMocks
    private PartidaServiceImpl service;

    @Mock
    private PartidaRepository repository;

    @Spy
    private ModelMapper mapper = new ModelMapper();

    @Test
    public void sucessoAoCriarTest() {
        PartidaDTO request = criarDtoRequest(LocalDateTime.now().minusDays(10));
        when(repository.save(any(Partida.class))).thenAnswer(invocationOnMock -> {
            Partida partida = invocationOnMock.getArgument(0);
            partida.setId(UUID.randomUUID().toString());
            return partida;
        });

        PartidaDTO response = service.criar(request);

        assertNotEquals(response.getId(), request.getId());
        assertEquals(response.getTimeMandante(), request.getTimeMandante());
        assertEquals(response.getTimeVisitante(), request.getTimeVisitante());
        assertEquals(response.getQuantidadeGolMandante(), request.getQuantidadeGolMandante());
        assertEquals(response.getQuantidadeGolVisitante(), request.getQuantidadeGolVisitante());
        assertEquals(response.getDataHora(), request.getDataHora());
        assertEquals(response.getEstadio(), request.getEstadio());
        verify(repository).save(any(Partida.class));
    }

    @Test
    public void erroHorarioNaoPermitidoAoCriarTest() {
        LocalDateTime dataHora = LocalDateTime.of(2023, 10, 10, 23, 30);
        PartidaDTO request = criarDtoRequest(dataHora);

        ParametrizedMessageException exception = assertThrows(ParametrizedMessageException.class, () -> service.criar(request));

        assertEquals(ConstantesUtil.ERRO_HORARIO_PARTIDA, exception.getMessage());
        verify(repository, never()).save(any(Partida.class));
    }

    @Test
    public void erroEstadioIndisponivelAoCriarTest() {
        PartidaDTO request = criarDtoRequest(LocalDateTime.now().minusDays(10));
        when(repository.verificarEstadio(request.getEstadio(), request.getDataHora().toLocalDate())).thenReturn(true);

        ParametrizedMessageException exception = assertThrows(ParametrizedMessageException.class, () -> service.criar(request));

        assertEquals(ConstantesUtil.ERRO_DISPONIBILIDADE_ESTADIO, exception.getMessage());
        verify(repository, never()).save(any(Partida.class));
    }

    @Test
    public void erroPartidaIndisponivelAoCriarTest() {
        PartidaDTO request = criarDtoRequest(LocalDateTime.now().minusDays(10));
        when(repository.verificarDisponibilidade(request, request.getDataHora().minusDays(2), request.getDataHora().plusDays(2))).thenReturn(true);

        ParametrizedMessageException exception = assertThrows(ParametrizedMessageException.class, () -> service.criar(request));

        assertEquals(ConstantesUtil.ERRO_DISPONIBILIDADE_PARTIDA, exception.getMessage());
        verify(repository, never()).save(any(Partida.class));
    }

    @Test
    public void erroPartidaNoFuturoAoCriarTest() {
        PartidaDTO request = criarDtoRequest(LocalDateTime.now().plusDays(10));

        ParametrizedMessageException exception = assertThrows(ParametrizedMessageException.class, () -> service.criar(request));

        assertEquals(ConstantesUtil.ERRO_PARTIDA_FUTURO, exception.getMessage());
        verify(repository, never()).save(any(Partida.class));
    }

    @Test
    public void sucessoAoAtualizarTest() {
        String id = UUID.randomUUID().toString();
        PartidaAtualizadaDTO request = criarDtoRequest();
        Partida get = criarPartida(id, LocalDateTime.now().minusDays(5));
        when(repository.findById(anyString())).thenReturn(Optional.of(get));
        when(repository.save(any(Partida.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        PartidaDTO response = service.atualizar(id, request);

        assertEquals(response.getTimeMandante(), get.getTimeMandante());
        assertEquals(response.getTimeVisitante(), get.getTimeVisitante());
        assertEquals(response.getQuantidadeGolMandante().intValue(), get.getQuantidadeGolMandante());
        assertEquals(response.getQuantidadeGolVisitante().intValue(), get.getQuantidadeGolVisitante());
        assertEquals(response.getDataHora(), get.getDataHora());
        assertNotEquals(response.getEstadio(), get.getEstadio());
        assertEquals(response.getEstadio(), request.getEstadio());
        verify(repository).findById(anyString());
        verify(repository).save(any(Partida.class));
    }

    @Test
    public void erroPartidaNaoEncontradaAoAtualizarTest() {
        String id = UUID.randomUUID().toString();
        PartidaAtualizadaDTO request = criarDtoRequest();
        when(repository.findById(id)).thenReturn(Optional.empty());

        ParametrizedMessageException exception = assertThrows(ParametrizedMessageException.class, () -> service.atualizar(id, request));

        assertEquals(ConstantesUtil.ERRO_PARTIDA_NAO_ENCONTRADA, exception.getMessage());
        verify(repository).findById(anyString());
        verify(repository, never()).save(any(Partida.class));
    }

    @Test
    public void sucessoAoListarTodosRegistrosTest() {
        LocalDateTime dataHora = LocalDateTime.now();
        Partida primeiraPartida = criarPartida(UUID.randomUUID().toString(), dataHora);
        Partida segundaPartida = criarPartida(UUID.randomUUID().toString(), dataHora.plusDays(5));
        Partida terceiraPartida = criarPartida(UUID.randomUUID().toString(), dataHora.plusDays(10));
        Mockito.when(repository.findAll()).thenReturn(List.of(primeiraPartida, segundaPartida, terceiraPartida));

        List<PartidaDTO> response = service.listarTodas();

        assertEquals(response.size(), 3);
        assertEquals(dataHora, response.get(0).getDataHora());
        assertNotEquals(dataHora, response.get(1).getDataHora());
        verify(repository).findAll();
    }

    @Test
    public void sucessoAoListarNenhumRegistroTest() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<PartidaDTO> response = service.listarTodas();

        assertTrue(response.isEmpty());
        verify(repository).findAll();
    }

    @Test
    public void sucessoAoListarTodasGoleadasTest() {
        LocalDateTime dataHora = LocalDateTime.now();
        Partida primeiraPartida = criarPartida(UUID.randomUUID().toString(), dataHora);
        Partida segundaPartida = criarPartida(UUID.randomUUID().toString(), dataHora.plusDays(5));
        when(repository.listarGoleadas()).thenReturn(List.of(primeiraPartida, segundaPartida));

        List<PartidaDTO> response = service.listarGoleadas();

        assertFalse(response.isEmpty());
        assertTrue(response.get(0).getQuantidadeGolMandante().subtract(response.get(0).getQuantidadeGolVisitante()).compareTo(BigDecimal.valueOf(3)) >= 0);
        assertTrue(response.get(1).getQuantidadeGolMandante().subtract(response.get(1).getQuantidadeGolVisitante()).compareTo(BigDecimal.valueOf(3)) >= 0);
        verify(repository).listarGoleadas();
    }

    @Test
    public void sucessoAoListarNenhumaGoleadaTest() {
        when(repository.listarGoleadas()).thenReturn(Collections.emptyList());

        List<PartidaDTO> response = service.listarGoleadas();

        assertTrue(response.isEmpty());
        verify(repository).listarGoleadas();
    }

    @Test
    public void sucessoAoListarTodasPartidasSemGolsTest() {
        LocalDateTime dataHora = LocalDateTime.now();
        Partida primeiraPartida = criarPartida(UUID.randomUUID().toString(), dataHora);
        primeiraPartida.setQuantidadeGolMandante(0);
        Partida segundaPartida = criarPartida(UUID.randomUUID().toString(), dataHora.plusDays(5));
        segundaPartida.setQuantidadeGolMandante(0);
        when(repository.listarPartidasSemGols()).thenReturn(List.of(primeiraPartida, segundaPartida));

        List<PartidaDTO> response = service.listarPartidasSemGols();

        assertFalse(response.isEmpty());
        assertTrue(response.get(0).getQuantidadeGolMandante().equals(BigDecimal.ZERO) && response.get(0).getQuantidadeGolVisitante().equals(BigDecimal.ZERO));
        assertTrue(response.get(1).getQuantidadeGolMandante().equals(BigDecimal.ZERO) && response.get(1).getQuantidadeGolVisitante().equals(BigDecimal.ZERO));
        verify(repository).listarPartidasSemGols();
    }

    @Test
    public void sucessoAoListarNenhumaPartidaSemGolsTest() {
        when(repository.listarPartidasSemGols()).thenReturn(Collections.emptyList());

        List<PartidaDTO> response = service.listarPartidasSemGols();

        Assertions.assertTrue(response.isEmpty());
        verify(repository).listarPartidasSemGols();
    }

    @Test
    public void succesoAoListarPartidasPorTimeSemFiltroTest() {
        String time = "FLU";
        Partida partida = criarPartida(UUID.randomUUID().toString(), LocalDateTime.now().minusDays(5));
        when(repository.listarPartidasPorTime(anyString(), isNull())).thenReturn(List.of(partida));

        List<PartidaDTO> response = service.listarPartidasPorTime(time, null);

        assertFalse(response.isEmpty());
        assertEquals(response.get(0).getTimeMandante(), time);
        verify(repository).listarPartidasPorTime(anyString(), isNull());
    }

    @Test
    public void succesoAoListarPartidasPorTimeComFiltroTest() {
        String time = "FLU";
        String filtro = "MANDANTE";
        Partida partida = criarPartida(UUID.randomUUID().toString(), LocalDateTime.now().minusDays(5));
        when(repository.listarPartidasPorTime(anyString(), anyString())).thenReturn(List.of(partida));

        List<PartidaDTO> response = service.listarPartidasPorTime(time, filtro);

        assertFalse(response.isEmpty());
        assertEquals(response.get(0).getTimeMandante(), time);
        verify(repository).listarPartidasPorTime(anyString(), anyString());
    }

    @Test
    public void succesoAoListarNenhumaPartidaPorTimeTest() {
        String time = "FLU";
        when(repository.listarPartidasPorTime(anyString(), isNull())).thenReturn(Collections.emptyList());

        List<PartidaDTO> response = service.listarPartidasPorTime(time, null);

        assertTrue(response.isEmpty());
        verify(repository).listarPartidasPorTime(anyString(), isNull());
    }

    @Test
    public void erroAoListarNenhumaPartidaPorTimeComFiltroInvalidoTest() {
        String time = "FLU";
        String filtro = "TESTE";

        ParametrizedMessageException exception = assertThrows(ParametrizedMessageException.class, () -> service.listarPartidasPorTime(time, filtro));

        assertEquals(ConstantesUtil.ERRO_VALOR_INVALIDO, exception.getMessage());
        verify(repository, never()).listarPartidasPorTime(anyString(), anyString());
    }

    @Test
    public void succesoAoListarPartidasPorEstadioTest() {
        String estadio = "Maracanã";
        Partida partida = criarPartida(UUID.randomUUID().toString(), LocalDateTime.now().minusDays(5));
        when(repository.findAllByEstadio(anyString())).thenReturn(List.of(partida));

        List<PartidaDTO> response = service.listarPartidasPorEstadio(estadio);

        assertFalse(response.isEmpty());
        assertEquals(response.get(0).getEstadio(), estadio);
        verify(repository).findAllByEstadio(anyString());
    }

    @Test
    public void succesoAoListarNenhumaPartidaPorEstadioTest() {
        String estadio = "Maracanã 2";
        when(repository.findAllByEstadio(anyString())).thenReturn(Collections.emptyList());

        List<PartidaDTO> response = service.listarPartidasPorEstadio(estadio);

        assertTrue(response.isEmpty());
        verify(repository).findAllByEstadio(anyString());
    }

    @Test
    public void succesoAoBuscarRetrospectoDoTimeSemFiltroTest() {
        String time = "FLU";
        when(repository.buscarRetrospectoTime(anyString(), isNull())).thenReturn(new RetrospectoClubeDTO(1L, 0L, 0L, 4L, 0L));

        RetrospectoClubeDTO response = service.buscarRetrospectoTime(time, null);

        assertEquals(response.getVitorias(), 1);
        assertEquals(response.getEmpates(), 0);
        assertEquals(response.getDerrotas(), 0);
        assertEquals(response.getGolsPro(), 4);
        assertEquals(response.getGolsSofrido(), 0);
        verify(repository).buscarRetrospectoTime(anyString(), isNull());
    }

    @Test
    public void succesoAoBuscarRetrospectoDoTimeComFiltroMandanteTest() {
        String time = "FLU";
        String filtro = "MANDANTE";
        when(repository.buscarRetrospectoTime(anyString(), anyString())).thenReturn(new RetrospectoClubeDTO(1L, 0L, 0L, 4L, 0L));

        RetrospectoClubeDTO response = service.buscarRetrospectoTime(time, filtro);

        assertEquals(response.getVitorias(), 1);
        assertEquals(response.getEmpates(), 0);
        assertEquals(response.getDerrotas(), 0);
        assertEquals(response.getGolsPro(), 4);
        assertEquals(response.getGolsSofrido(), 0);
        verify(repository).buscarRetrospectoTime(anyString(), anyString());
    }

    @Test
    public void succesoAoBuscarRetrospectoDoTimeComFiltroVisitanteTest() {
        String time = "FLU";
        String filtro = "VISITANTE";
        when(repository.buscarRetrospectoTime(anyString(), anyString())).thenReturn(new RetrospectoClubeDTO(0L, 0L, 0L, 0L, 0L));

        RetrospectoClubeDTO response = service.buscarRetrospectoTime(time, filtro);

        assertEquals(response.getVitorias(), 0);
        assertEquals(response.getEmpates(), 0);
        assertEquals(response.getDerrotas(), 0);
        assertEquals(response.getGolsPro(), 0);
        assertEquals(response.getGolsSofrido(), 0);
        verify(repository).buscarRetrospectoTime(anyString(), anyString());
    }

    @Test
    public void erroAoBuscarRetrospectoDoTimeComFiltroInvalidoTest() {
        String time = "FLU";
        String filtro = "TESTE";

        ParametrizedMessageException exception = assertThrows(ParametrizedMessageException.class, () -> service.buscarRetrospectoTime(time, filtro));

        assertEquals(ConstantesUtil.ERRO_VALOR_INVALIDO, exception.getMessage());
        verify(repository, never()).buscarRetrospectoTime(anyString(), anyString());
    }

    @Test
    public void succesoAoBuscarRetrospectoDoConfrontoSemFiltroTest() {
        String primeiroTime = "FLU";
        String segundoTime = "FLA";
        when(repository.buscarRetrospectoConfronto(anyString(), anyString(), isNull())).thenReturn(new RetrospectoConfrontoDTO(1L, 0L, 0L, 4L, 0L));

        RetrospectoConfrontoDTO response = service.buscarRetrospectoConfronto(primeiroTime, segundoTime, null);

        assertEquals(response.getVitoriasPrimeiroTime(), 1);
        assertEquals(response.getVitoriasSegundoTime(), 0);
        assertEquals(response.getEmpates(), 0);
        assertEquals(response.getGolsPrimeiroTime(), 4);
        assertEquals(response.getGolsSegundoTime(), 0);
        verify(repository).buscarRetrospectoConfronto(anyString(), anyString(), isNull());
    }

    @Test
    public void succesoAoBuscarRetrospectoDoConfrontoComFiltroPrimeiroTest() {
        String primeiroTime = "FLU";
        String segundoTime = "FLA";
        String filtro = "PRIMEIRO";
        when(repository.buscarRetrospectoConfronto(anyString(), anyString(), anyString())).thenReturn(new RetrospectoConfrontoDTO(1L, 0L, 0L, 4L, 0L));

        RetrospectoConfrontoDTO response = service.buscarRetrospectoConfronto(primeiroTime, segundoTime, filtro);

        assertEquals(response.getVitoriasPrimeiroTime(), 1);
        assertEquals(response.getVitoriasSegundoTime(), 0);
        assertEquals(response.getEmpates(), 0);
        assertEquals(response.getGolsPrimeiroTime(), 4);
        assertEquals(response.getGolsSegundoTime(), 0);
        verify(repository).buscarRetrospectoConfronto(anyString(), anyString(), anyString());
    }

    @Test
    public void succesoAoBuscarRetrospectoDoConfrontoComFiltroSegundoTest() {
        String primeiroTime = "FLU";
        String segundoTime = "FLA";
        String filtro = "SEGUNDO";
        when(repository.buscarRetrospectoConfronto(anyString(), anyString(), anyString())).thenReturn(new RetrospectoConfrontoDTO(0L, 0L, 0L, 0L, 0L));

        RetrospectoConfrontoDTO response = service.buscarRetrospectoConfronto(primeiroTime, segundoTime, filtro);

        assertEquals(response.getVitoriasPrimeiroTime(), 0);
        assertEquals(response.getVitoriasSegundoTime(), 0);
        assertEquals(response.getEmpates(), 0);
        assertEquals(response.getGolsPrimeiroTime(), 0);
        assertEquals(response.getGolsSegundoTime(), 0);
        verify(repository).buscarRetrospectoConfronto(anyString(), anyString(), anyString());
    }

    @Test
    public void erroAoBuscarRetrospectoDoConfrontoComFiltroInvalidoTest() {
        String primeiroTime = "FLU";
        String segundoTime = "FLA";
        String filtro = "TESTE";

        ParametrizedMessageException exception = assertThrows(ParametrizedMessageException.class, () -> service.buscarRetrospectoConfronto(primeiroTime, segundoTime, filtro));

        assertEquals(ConstantesUtil.ERRO_VALOR_INVALIDO, exception.getMessage());
        verify(repository, never()).buscarRetrospectoConfronto(anyString(), anyString(), anyString());
    }

    @Test
    public void sucessoAoListarFreguesesTest() {
        String time = "FLU";
        when(repository.listarFregueses(anyString(), any(PageRequest.class))).thenReturn(List.of(new FreguesDTO("FLA", 1L, 1L, 0L)));

        List<FreguesDTO> response = service.listarFregueses(time);

        assertFalse(response.isEmpty());
        assertEquals(response.get(0).getTime(), "FLA");
        assertEquals(response.get(0).getTotalPartidas(), 1);
        assertEquals(response.get(0).getVitorias(), 1);
        assertEquals(response.get(0).getDerrotas(), 0);
        verify(repository).listarFregueses(anyString(), any(PageRequest.class));
    }

    @Test
    public void sucessoAoExcluirPartidaTest() {
        Partida partida = criarPartida(UUID.randomUUID().toString(), LocalDateTime.now().minusDays(5));
        when(repository.findById(anyString())).thenReturn(Optional.of(partida));

        service.excluir(partida.getId());

        verify(repository).findById(anyString());
        verify(repository).delete(any(Partida.class));
    }

    @Test
    public void erroAoExcluirPartidaNaoEncontradaTest() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        ParametrizedMessageException exception = assertThrows(ParametrizedMessageException.class, () -> service.excluir(UUID.randomUUID().toString()));

        assertEquals(ConstantesUtil.ERRO_PARTIDA_NAO_ENCONTRADA, exception.getMessage());
        verify(repository).findById(anyString());
        verify(repository, never()).delete(any(Partida.class));
    }

    private Partida criarPartida(String id, LocalDateTime dataHora) {
        Partida partida = new Partida();
        partida.setId(id);
        partida.setTimeMandante("FLU");
        partida.setTimeVisitante("FLA");
        partida.setQuantidadeGolMandante(4);
        partida.setQuantidadeGolVisitante(0);
        partida.setDataHora(dataHora);
        partida.setEstadio("Maracanã");
        return partida;
    }

    private PartidaDTO criarDtoRequest(LocalDateTime dataHora) {
        PartidaDTO dto = new PartidaDTO();
        dto.setTimeMandante("FLU");
        dto.setTimeVisitante("FLA");
        dto.setQuantidadeGolMandante(BigDecimal.valueOf(4));
        dto.setQuantidadeGolVisitante(BigDecimal.ZERO);
        dto.setDataHora(dataHora);
        dto.setEstadio("Maracanã");
        return dto;
    }

    private PartidaAtualizadaDTO criarDtoRequest() {
        PartidaAtualizadaDTO dto = new PartidaAtualizadaDTO();
        dto.setEstadio("Outro");
        return dto;
    }

}
