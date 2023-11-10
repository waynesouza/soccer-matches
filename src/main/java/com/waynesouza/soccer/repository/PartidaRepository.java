package com.waynesouza.soccer.repository;

import com.waynesouza.soccer.domain.Partida;
import com.waynesouza.soccer.domain.dto.PartidaDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, String> {

    @Query("select count(p) > 0 from Partida p " +
            "where (p.estadio = :estadio and function('date', p.dataHora) = :data)")
    Boolean verificarEstadio(@Param("estadio") String estadio,
                             @Param("data") LocalDate data);

    @Query("select count(p) > 0 from Partida p " +
            "where (" +
                "coalesce(:#{#dto.id}, null) is null or p.id != :#{#dto.id}" +
            ") " +
            "and (" +
                "p.timeMandante in (:#{#dto.timeMandante}, :#{#dto.timeVisitante}) or " +
                "p.timeVisitante in (:#{#dto.timeMandante}, :#{#dto.timeVisitante})" +
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
                "(:filtro = 'MANDANTE' and p.timeMandante = :time) or " +
                "(:filtro = 'VISITANTE' and p.timeVisitante = :time)" +
            ") " +
            "or (" +
                "(:filtro is null) and " +
                "(p.timeMandante = :time or p.timeVisitante = :time)" +
            ")")
    List<Partida> listarPartidasPorTime(@Param("time") String time,
                                        @Param("filtro") String filtro);

}
