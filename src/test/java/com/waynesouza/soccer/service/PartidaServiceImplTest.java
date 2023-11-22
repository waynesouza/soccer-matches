package com.waynesouza.soccer.service;

import com.waynesouza.soccer.config.exception.ParametrizedMessageException;
import com.waynesouza.soccer.domain.Partida;
import com.waynesouza.soccer.domain.dto.PartidaAtualizadaDTO;
import com.waynesouza.soccer.domain.dto.PartidaDTO;
import com.waynesouza.soccer.repository.PartidaRepository;
import com.waynesouza.soccer.service.impl.PartidaServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PartidaServiceImplTest {

    @InjectMocks
    private PartidaServiceImpl service;

    @Mock
    private PartidaRepository repository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private Configuration configuration;

    @Test
    public void sucessoAoCriarTest() {
        LocalDateTime dataHora = LocalDateTime.now().minusDays(10);
        PartidaDTO dto = criarDTO(dataHora);
        Partida entidade = criarPartida(dataHora);
        Mockito.when(mapper.map(dto, Partida.class)).thenReturn(entidade);
        Mockito.when(repository.save(entidade)).thenReturn(entidade);
        Mockito.when(mapper.map(entidade, PartidaDTO.class)).thenReturn(dto);

        PartidaDTO partidaSalvaDTO = service.criar(dto);

        Assertions.assertEquals(partidaSalvaDTO, dto);
    }

    @Test
    public void erroHorarioNaoPermitidoAoCriarTest() {
        LocalDateTime dataHora = LocalDateTime.of(2023, 10, 10, 23, 30);
        PartidaDTO dto = criarDTO(dataHora);

        Assertions.assertThrows(ParametrizedMessageException.class, () -> service.criar(dto));
    }

    @Test
    public void erroEstadioIndisponivelAoCriarTest() {
        LocalDateTime dataHora = LocalDateTime.now().minusDays(10);
        PartidaDTO dto = criarDTO(dataHora);
        Mockito.when(repository.verificarEstadio(dto.getEstadio(), dto.getDataHora().toLocalDate())).thenReturn(true);

        Assertions.assertThrows(ParametrizedMessageException.class, () -> service.criar(dto));
    }

    @Test
    public void erroPartidaIndisponivelAoCriarTest() {
        LocalDateTime dataHora = LocalDateTime.now().minusDays(10);
        PartidaDTO dto = criarDTO(dataHora);
        Mockito.when(repository.verificarDisponibilidade(dto, dto.getDataHora().minusDays(2), dto.getDataHora().plusDays(2))).thenReturn(true);

        Assertions.assertThrows(ParametrizedMessageException.class, () -> service.criar(dto));
    }

    @Test
    public void erroPartidaNoFuturoAoCriarTest() {
        LocalDateTime dataHora = LocalDateTime.now().plusDays(10);
        PartidaDTO dto = criarDTO(dataHora);

        Assertions.assertThrows(ParametrizedMessageException.class, () -> service.criar(dto));
    }

    @Test
    public void sucessoAoAtualizarTest() {
        String id = "id";
        LocalDateTime dataHora = LocalDateTime.now().minusDays(10);
        PartidaAtualizadaDTO partidaAtualizada = PartidaAtualizadaDTO.builder().estadio("Mineirão").build();
        PartidaDTO dto = criarDTO(dataHora);
        Partida entidade = criarPartida(dataHora);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(entidade));
        Mockito.when(mapper.map(entidade, PartidaDTO.class)).thenReturn(dto);
        Mockito.when(mapper.getConfiguration()).thenReturn(configuration);
        dto.setEstadio(partidaAtualizada.getEstadio());
        Mockito.doNothing().when(mapper).map(partidaAtualizada, dto);
        Mockito.when(mapper.map(dto, Partida.class)).thenReturn(entidade);
        Mockito.when(repository.save(entidade)).thenReturn(entidade);
        Mockito.when(mapper.map(entidade, PartidaDTO.class)).thenReturn(dto);

        PartidaDTO partidaAtualizadaDTO = service.atualizar(id, partidaAtualizada);

        Assertions.assertEquals(partidaAtualizadaDTO.getEstadio(), partidaAtualizada.getEstadio());
    }

    @Test
    public void erroPartidaNaoEncontradaAoAtualizarTest() {
        String id = "id";
        LocalDateTime dataHora = LocalDateTime.now().minusDays(10);
        PartidaAtualizadaDTO partidaAtualizada = PartidaAtualizadaDTO.builder().estadio("Mineirão").build();
        Mockito.when(repository.findById(id)).thenThrow(ParametrizedMessageException.class);

        Assertions.assertThrows(ParametrizedMessageException.class, () -> service.atualizar(id, partidaAtualizada));
    }

    @Test
    public void erroHorarioNaoPermitidoAoAtualizarTest() {
        String id = "id";
        LocalDateTime dataHora = LocalDateTime.now().minusDays(10);
        PartidaAtualizadaDTO partidaAtualizada = PartidaAtualizadaDTO.builder()
                .dataHora(LocalDateTime.of(2023, 10, 10, 23, 30))
                .build();
        PartidaDTO dto = criarDTO(dataHora);
        Partida entidade = criarPartida(dataHora);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(entidade));
        Mockito.when(mapper.map(entidade, PartidaDTO.class)).thenReturn(dto);
        Mockito.when(mapper.getConfiguration()).thenReturn(configuration);
        dto.setDataHora(partidaAtualizada.getDataHora());
        Mockito.doNothing().when(mapper).map(partidaAtualizada, dto);

        Assertions.assertThrows(ParametrizedMessageException.class, () -> service.atualizar(id, partidaAtualizada));
    }

    @Test
    public void erroEstadioIndisponivelAoAtualizarTest() {
        String id = "id";
        LocalDateTime dataHora = LocalDateTime.now().minusDays(10);
        PartidaAtualizadaDTO partidaAtualizada = PartidaAtualizadaDTO.builder()
                .estadio("Arena MRV")
                .build();
        PartidaDTO dto = criarDTO(dataHora);
        Partida entidade = criarPartida(dataHora);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(entidade));
        Mockito.when(mapper.map(entidade, PartidaDTO.class)).thenReturn(dto);
        Mockito.when(mapper.getConfiguration()).thenReturn(configuration);
        dto.setEstadio(partidaAtualizada.getEstadio());
        Mockito.doNothing().when(mapper).map(partidaAtualizada, dto);
        Mockito.when(repository.verificarEstadio(dto.getEstadio(), dto.getDataHora().toLocalDate())).thenReturn(true);

        Assertions.assertThrows(ParametrizedMessageException.class, () -> service.atualizar(id, partidaAtualizada));
    }

    @Test
    public void erroPartidaIndisponivelAoAtualizarTest() {
        String id = "id";
        LocalDateTime dataHora = LocalDateTime.now().minusDays(10);
        PartidaAtualizadaDTO partidaAtualizada = PartidaAtualizadaDTO.builder()
                .dataHora(dataHora)
                .build();
        PartidaDTO dto = criarDTO(dataHora);
        Partida entidade = criarPartida(dataHora);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(entidade));
        Mockito.when(mapper.map(entidade, PartidaDTO.class)).thenReturn(dto);
        Mockito.when(mapper.getConfiguration()).thenReturn(configuration);
        dto.setEstadio(partidaAtualizada.getEstadio());
        Mockito.doNothing().when(mapper).map(partidaAtualizada, dto);
        Mockito.when(repository.verificarDisponibilidade(dto, dto.getDataHora().minusDays(2), dto.getDataHora().plusDays(2))).thenReturn(true);

        Assertions.assertThrows(ParametrizedMessageException.class, () -> service.atualizar(id, partidaAtualizada));
    }

    @Test
    public void erroPartidaNoFuturoAoAtualizarTest() {
        String id = "id";
        LocalDateTime dataHora = LocalDateTime.now().minusDays(10);
        PartidaAtualizadaDTO partidaAtualizada = PartidaAtualizadaDTO.builder()
                .dataHora(LocalDateTime.now().plusDays(20))
                .build();
        PartidaDTO dto = criarDTO(dataHora);
        Partida entidade = criarPartida(dataHora);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(entidade));
        Mockito.when(mapper.map(entidade, PartidaDTO.class)).thenReturn(dto);
        Mockito.when(mapper.getConfiguration()).thenReturn(configuration);
        dto.setDataHora(partidaAtualizada.getDataHora());
        Mockito.doNothing().when(mapper).map(partidaAtualizada, dto);

        Assertions.assertThrows(ParametrizedMessageException.class, () -> service.atualizar(id, partidaAtualizada));
    }

    @Test
    public void sucessoAoListarTodosRegistrosTest() {
        LocalDateTime dataHora = LocalDateTime.now();
        Partida primeiraPartida = criarPartida(dataHora);
        Partida segundaPartida = criarPartida(dataHora.plusDays(5));
        Partida terceiraPartida = criarPartida(dataHora.plusDays(10));
        PartidaDTO primeiraPartidaDTO = criarDTO(dataHora);
        PartidaDTO segundaPartidaDTO = criarDTO(dataHora.plusDays(5));
        PartidaDTO terceiraPartidaDTO = criarDTO(dataHora.plusDays(10));
        Mockito.when(repository.findAll()).thenReturn(List.of(primeiraPartida, segundaPartida, terceiraPartida));
        Mockito.when(mapper.map(primeiraPartida, PartidaDTO.class)).thenReturn(primeiraPartidaDTO);
        Mockito.when(mapper.map(segundaPartida, PartidaDTO.class)).thenReturn(segundaPartidaDTO);
        Mockito.when(mapper.map(terceiraPartida, PartidaDTO.class)).thenReturn(terceiraPartidaDTO);

        List<PartidaDTO> partidas = service.listarTodas();

        Assertions.assertFalse(partidas.isEmpty());
        Assertions.assertEquals(3, partidas.size());
        Assertions.assertEquals(dataHora, partidas.get(0).getDataHora());
        Assertions.assertNotEquals(dataHora, partidas.get(1).getDataHora());
    }

    @Test
    public void sucessoAoListarNenhumRegistroTest() {
        Mockito.when(repository.findAll()).thenReturn(List.of());

        List<PartidaDTO> partidas = service.listarTodas();

        Assertions.assertTrue(partidas.isEmpty());
    }

    @Test
    public void sucessoAoListarTodasGoleadasTest() {
        LocalDateTime dataHora = LocalDateTime.now();
        Partida primeiraPartida = criarPartida(dataHora);
        Partida segundaPartida = criarPartida(dataHora.plusDays(5));
        PartidaDTO primeiraPartidaDTO = criarDTO(dataHora);
        PartidaDTO segundaPartidaDTO = criarDTO(dataHora.plusDays(5));
        Mockito.when(repository.listarGoleadas()).thenReturn(List.of(primeiraPartida, segundaPartida));
        Mockito.when(mapper.map(primeiraPartida, PartidaDTO.class)).thenReturn(primeiraPartidaDTO);
        Mockito.when(mapper.map(segundaPartida, PartidaDTO.class)).thenReturn(segundaPartidaDTO);

        List<PartidaDTO> partidas = service.listarGoleadas();

        Assertions.assertFalse(partidas.isEmpty());
        Assertions.assertTrue(partidas.get(0).getQuantidadeGolMandante() - partidas.get(0).getQuantidadeGolVisitante() >= 3);
        Assertions.assertTrue(partidas.get(1).getQuantidadeGolMandante() - partidas.get(1).getQuantidadeGolVisitante() >= 3);
    }

    @Test
    public void sucessoAoListarNenhumaGoleadaTest() {
        Mockito.when(repository.listarGoleadas()).thenReturn(List.of());

        List<PartidaDTO> partidas = service.listarGoleadas();

        Assertions.assertTrue(partidas.isEmpty());
    }

    @Test
    public void sucessoAoListarTodasPartidasSemGolsTest() {
        LocalDateTime dataHora = LocalDateTime.now();
        Partida primeiraPartida = criarPartida(dataHora);
        primeiraPartida.setQuantidadeGolMandante(0);
        Partida segundaPartida = criarPartida(dataHora.plusDays(5));
        segundaPartida.setQuantidadeGolMandante(0);
        PartidaDTO primeiraPartidaDTO = criarDTO(dataHora);
        primeiraPartidaDTO.setQuantidadeGolMandante(0);
        PartidaDTO segundaPartidaDTO = criarDTO(dataHora.plusDays(5));
        segundaPartidaDTO.setQuantidadeGolMandante(0);
        Mockito.when(repository.listarPartidasSemGols()).thenReturn(List.of(primeiraPartida, segundaPartida));
        Mockito.when(mapper.map(primeiraPartida, PartidaDTO.class)).thenReturn(primeiraPartidaDTO);
        Mockito.when(mapper.map(segundaPartida, PartidaDTO.class)).thenReturn(segundaPartidaDTO);

        List<PartidaDTO> partidas = service.listarPartidasSemGols();

        Assertions.assertFalse(partidas.isEmpty());
        Assertions.assertTrue(partidas.get(0).getQuantidadeGolMandante() == 0 && partidas.get(0).getQuantidadeGolVisitante() == 0);
        Assertions.assertTrue(partidas.get(1).getQuantidadeGolMandante() == 0 && partidas.get(1).getQuantidadeGolVisitante() == 0);
    }

    @Test
    public void sucessoAoListarNenhumaPartidaSemGolsTest() {
        Mockito.when(repository.listarPartidasSemGols()).thenReturn(List.of());

        List<PartidaDTO> partidas = service.listarPartidasSemGols();

        Assertions.assertTrue(partidas.isEmpty());
    }

    @Test
    public void sucessoAoListarTodasPartidasPorTimeTest() {
        LocalDateTime dataHora = LocalDateTime.now();
        Partida primeiraPartida = criarPartida(dataHora);
        primeiraPartida.setQuantidadeGolMandante(0);
        Partida segundaPartida = criarPartida(dataHora.plusDays(5));
        segundaPartida.setQuantidadeGolMandante(0);
        PartidaDTO primeiraPartidaDTO = criarDTO(dataHora);
        primeiraPartidaDTO.setQuantidadeGolMandante(0);
        PartidaDTO segundaPartidaDTO = criarDTO(dataHora.plusDays(5));
        segundaPartidaDTO.setQuantidadeGolMandante(0);
        Mockito.when(repository.listarPartidasSemGols()).thenReturn(List.of(primeiraPartida, segundaPartida));
        Mockito.when(mapper.map(primeiraPartida, PartidaDTO.class)).thenReturn(primeiraPartidaDTO);
        Mockito.when(mapper.map(segundaPartida, PartidaDTO.class)).thenReturn(segundaPartidaDTO);

        List<PartidaDTO> partidas = service.listarPartidasSemGols();

        Assertions.assertFalse(partidas.isEmpty());
        Assertions.assertTrue(partidas.get(0).getQuantidadeGolMandante() == 0 && partidas.get(0).getQuantidadeGolVisitante() == 0);
        Assertions.assertTrue(partidas.get(1).getQuantidadeGolMandante() == 0 && partidas.get(1).getQuantidadeGolVisitante() == 0);
    }

    private PartidaDTO criarDTO(LocalDateTime dataHora) {
        return PartidaDTO.builder()
                .timeMandante("Fluminense")
                .timeVisitante("Flamengo")
                .quantidadeGolMandante(4)
                .quantidadeGolVisitante(0)
                .dataHora(dataHora)
                .estadio("Maracanã")
                .build();
    }

    private Partida criarPartida(LocalDateTime dataHora) {
        Partida partida = new Partida();
        partida.setTimeMandante("Fluminense");
        partida.setTimeVisitante("Flamengo");
        partida.setQuantidadeGolMandante(4);
        partida.setQuantidadeGolVisitante(0);
        partida.setDataHora(dataHora);
        partida.setEstadio("Maracanã");
        return partida;
    }

}
