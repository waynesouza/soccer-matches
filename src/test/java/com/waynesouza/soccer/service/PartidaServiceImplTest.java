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
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PartidaServiceImplTest {

    @InjectMocks
    private PartidaServiceImpl service;

    @Mock
    private PartidaRepository repository;

    @Mock
    private ModelMapper mapper;

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

//    @Test
//    public void sucessoAoAtualizarTest() {
//        String id = "id";
//        LocalDateTime dataHora = LocalDateTime.now().minusDays(10);
//        PartidaAtualizadaDTO partidaAtualizada = PartidaAtualizadaDTO.builder().estadio("Mineirão").build();
//        PartidaDTO dto = criarDTO(dataHora);
//        Partida entidade = criarPartida(dataHora);
//        Mockito.when(repository.findById(id)).thenReturn(Optional.of(entidade));
//        Mockito.when(mapper.map(entidade, PartidaDTO.class)).thenReturn(dto);
//        dto.setEstadio(partidaAtualizada.getEstadio());
//        Mockito.when(mapper.map(partidaAtualizada, PartidaDTO.class)).thenReturn(dto);
//        Mockito.when(mapper.map(dto, Partida.class)).thenReturn(entidade);
//        Mockito.when(repository.save(entidade)).thenReturn(entidade);
//        Mockito.when(mapper.map(entidade, PartidaDTO.class)).thenReturn(dto);
//
//        PartidaDTO partidaAtualizadaDTO = service.atualizar(id, partidaAtualizada);
//
//        Assertions.assertEquals(partidaAtualizadaDTO.getEstadio(), partidaAtualizada.getEstadio());
//    }

    private PartidaDTO criarDTO(LocalDateTime dataHora) {
        return PartidaDTO.builder()
                .timeMandante("Fluminense")
                .timeVisitante("Flamengo")
                .quantidadeGolMandante(2)
                .quantidadeGolVisitante(0)
                .dataHora(dataHora)
                .estadio("Maracanã")
                .build();
    }

    private Partida criarPartida(LocalDateTime dataHora) {
        Partida partida = new Partida();
        partida.setTimeMandante("Fluminense");
        partida.setTimeVisitante("Flamengo");
        partida.setQuantidadeGolMandante(2);
        partida.setQuantidadeGolVisitante(0);
        partida.setDataHora(dataHora);
        partida.setEstadio("Maracanã");
        return partida;
    }

}
