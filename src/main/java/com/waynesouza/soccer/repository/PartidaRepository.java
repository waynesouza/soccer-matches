package com.waynesouza.soccer.repository;

import com.waynesouza.soccer.domain.Partida;
import com.waynesouza.soccer.domain.dto.FreguesDTO;
import com.waynesouza.soccer.domain.dto.PartidaDTO;
import com.waynesouza.soccer.domain.dto.RetrospectoClubeDTO;
import com.waynesouza.soccer.domain.dto.RetrospectoConfrontoDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, String> {

    @Query("select count(p) > 1 from Partida p " +
            "where (p.estadio.nome = :estadio and function('date', p.dataHora) = :data)")
    Boolean verificarEstadio(@Param("estadio") String estadio,
                             @Param("data") LocalDate data);

    @Query("select count(p) > 0 from Partida p " +
            "where (" +
                "coalesce(:#{#dto.id}, null) is null or p.id != :#{#dto.id}" +
            ") " +
            "and (" +
                "p.equipeMandante.nome in (:#{#dto.equipeMandante}, :#{#dto.equipeVisitante}) or " +
                "p.equipeVisitante.nome in (:#{#dto.equipeMandante}, :#{#dto.equipeVisitante})" +
            ") " +
            "and (" +
                "(p.dataHora between :dataLimiteInferior and :dataLimiteSuperior)" +
            ")")
    Boolean verificarDisponibilidade(@Param("dto") PartidaDTO dto,
                                     @Param("dataLimiteInferior")LocalDateTime dataLimiteInferior,
                                     @Param("dataLimiteSuperior")LocalDateTime dataLimiteSuperior);

    @Query("select p from Partida p " +
            "where p.quantidadeGolMandante - p.quantidadeGolVisitante >= 3 or " +
            "p.quantidadeGolVisitante - p.quantidadeGolMandante >= 3")
    List<Partida> listarGoleadas();

    @Query("select p from Partida p " +
            "where p.quantidadeGolMandante = 0 and p.quantidadeGolVisitante = 0")
    List<Partida> listarPartidasSemGols();

    @Query("select p from Partida p " +
            "where (" +
                "(:filtro = 'MANDANTE' and p.equipeMandante.nome = :equipe) or " +
                "(:filtro = 'VISITANTE' and p.equipeVisitante.nome = :equipe)" +
            ") " +
            "or (" +
                "(:filtro is null) and " +
                "(p.equipeMandante.nome = :equipe or p.equipeVisitante.nome = :equipe)" +
            ")")
    List<Partida> listarPartidasPorTime(@Param("equipe") String equipe,
                                        @Param("filtro") String filtro);

    @Query("select p from Partida p where p.estadio.nome = :estadio")
    List<Partida> findAllByEstadio(@Param("estadio") String estadio);

    @Query("select new com.waynesouza.soccer.domain.dto.RetrospectoClubeDTO(" +
                "sum(case " +
                    "when p.equipeMandante.nome = :equipe and p.resultado = 'V' then 1 " +
                    "when p.equipeVisitante.nome = :equipe and p.resultado = 'D' then 1 " +
                    "else 0 " +
                "end), " +
                "sum(case " +
                    "when (p.equipeMandante.nome = :equipe or p.equipeVisitante.nome = :equipe) and p.resultado = 'E' then 1 " +
                    "else 0 " +
                "end), " +
                "sum(case " +
                    "when p.equipeMandante.nome = :equipe and p.resultado = 'D' then 1 " +
                    "when p.equipeVisitante.nome = :equipe and p.resultado = 'V' then 1 " +
                    "else 0 " +
                "end), " +
                "sum(case " +
                    "when p.equipeMandante.nome = :equipe then p.quantidadeGolMandante " +
                    "when p.equipeVisitante.nome = :equipe then p.quantidadeGolVisitante " +
                    "else 0 " +
                "end), " +
                "sum(case " +
                    "when p.equipeMandante.nome = :equipe then p.quantidadeGolVisitante " +
                    "when p.equipeVisitante.nome = :equipe then p.quantidadeGolMandante " +
                    "else 0 " +
                "end)" +
            ") " +
            "from Partida p " +
            "where (" +
                "(:filtro = 'MANDANTE' and p.equipeMandante.nome = :equipe) or " +
                "(:filtro = 'VISITANTE' and p.equipeVisitante.nome = :equipe)" +
            ") or " +
            "(" +
                ":filtro is null and " +
                "(p.equipeMandante.nome = :equipe or p.equipeVisitante.nome = :equipe)" +
            ")")
    RetrospectoClubeDTO buscarRetrospectoTime(@Param("equipe") String equipe,
                                              @Param("filtro") String filtro);

    @Query("select new com.waynesouza.soccer.domain.dto.RetrospectoConfrontoDTO(" +
                "sum(case " +
                    "when p.equipeMandante.nome = :primeiraEquipe and p.resultado = 'V' then 1 " +
                    "when p.equipeVisitante.nome = :primeiraEquipe and p.resultado = 'D' then 1 " +
                    "else 0 " +
                "end), " +
                "sum(case " +
                    "when p.equipeMandante.nome = :segundaEquipe and p.resultado = 'V' then 1 " +
                    "when p.equipeVisitante.nome = :segundaEquipe and p.resultado = 'D' then 1 " +
                    "else 0 " +
                "end), " +
                "sum(case when p.resultado = 'E' then 1 else 0 end), " +
                "sum(case " +
                    "when p.equipeMandante.nome = :primeiraEquipe then p.quantidadeGolMandante " +
                    "when p.equipeVisitante.nome = :primeiraEquipe then p.quantidadeGolVisitante " +
                    "else 0 " +
                "end), " +
                "sum(case " +
                    "when p.equipeMandante.nome = :segundaEquipe then p.quantidadeGolMandante " +
                    "when p.equipeVisitante.nome = :segundaEquipe then p.quantidadeGolVisitante " +
                    "else 0 " +
                "end)" +
            ") " +
            "from Partida p " +
            "where (" +
                "(:filtro = 'PRIMEIRO' and p.equipeMandante.nome = :primeiraEquipe and p.equipeVisitante.nome = :segundaEquipe) or " +
                "(:filtro = 'SEGUNDO' and p.equipeMandante.nome = :segundaEquipe and p.equipeVisitante.nome = :primeiraEquipe)" +
            ") or " +
            "(" +
                ":filtro is null and " +
                "((p.equipeMandante.nome = :primeiraEquipe and p.equipeVisitante.nome = :segundaEquipe) or " +
                "(p.equipeMandante.nome = :segundaEquipe and p.equipeVisitante.nome = :primeiraEquipe))" +
            ")")
    RetrospectoConfrontoDTO buscarRetrospectoConfronto(@Param("primeiraEquipe") String primeiraEquipe,
                                                       @Param("segundaEquipe") String segundaEquipe,
                                                       @Param("filtro") String filtro);

    @Query("select new com.waynesouza.soccer.domain.dto.FreguesDTO(" +
            "case when p.equipeMandante.nome = :equipe then p.equipeVisitante.nome else p.equipeMandante.nome end, " +
            "count(p.id), " +
            "sum(case " +
                "when p.equipeMandante.nome = :equipe and p.resultado = 'V' then 1 " +
                "when p.equipeVisitante.nome = :equipe and p.resultado = 'D' then 1 " +
                "else 0 " +
            "end), " +
            "sum(case " +
                "when p.equipeMandante.nome = :equipe and p.resultado = 'D' then 1 " +
                "when p.equipeVisitante.nome = :equipe and p.resultado = 'V' then 1 " +
                "else 0 " +
            "end)) " +
            "from Partida p " +
            "where (p.equipeMandante.nome = :equipe or p.equipeVisitante.nome = :equipe) " +
            "group by case when p.equipeMandante.nome = :equipe then p.equipeVisitante.nome else p.equipeMandante.nome end " +
            "having " +
                "sum(case " +
                    "when p.equipeMandante.nome = :equipe and p.resultado = 'V' then 1 " +
                    "when p.equipeVisitante.nome = :equipe and p.resultado = 'D' then 1 " +
                    "else 0 " +
                "end) - " +
                "sum(case " +
                    "when p.equipeMandante.nome = :equipe and p.resultado = 'D' then 1 " +
                    "when p.equipeVisitante.nome = :equipe and p.resultado = 'V' then 1 " +
                    "else 0 " +
                "end) > 0 " +
            "order by " +
                "sum(case " +
                    "when p.equipeMandante.nome = :equipe and p.resultado = 'V' then 1 " +
                    "when p.equipeVisitante.nome = :equipe and p.resultado = 'D' then 1 " +
                    "else 0 " +
                "end) - " +
                "sum(case " +
                    "when p.equipeMandante.nome = :equipe and p.resultado = 'D' then 1 " +
                    "when p.equipeVisitante.nome = :equipe and p.resultado = 'V' then 1 " +
                    "else 0 " +
                "end) " +
            "desc")
    List<FreguesDTO> listarFregueses(@Param("equipe") String equipe, Pageable pageable);

}
