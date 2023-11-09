package com.waynesouza.soccer.repository;

import com.waynesouza.soccer.domain.Partida;
import com.waynesouza.soccer.dto.PartidaDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
                                     @Param("data") LocalDate data,
                                     @Param("dataLimiteInferior")LocalDateTime dataLimiteInferior,
                                     @Param("dataLimiteSuperior")LocalDateTime dataLimiteSuperior);

}
