package com.waynesouza.soccer.repository;

import com.waynesouza.soccer.domain.Partida;
import com.waynesouza.soccer.service.dto.PartidaDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, UUID> {

    @Query("select count(p) > 0 from Partida p " +
            "where (" +
                "coalesce(:#{#dto.id}, null) is null or p.id != :#{#dto.id}" +
            ") " +
            "and (" +
                "p.estadio = :#{#dto.estadio} and p.data = :#{#dto.data}" +
            ")" +
            "and (" +
                "p.timeMandante in (:#{#dto.timeMandante}, :#{#dto.timeVisitante}) or " +
                "p.timeVisitante in (:#{#dto.timeMandante}, :#{#dto.timeVisitante})" +
            ") " +
            "and (" +
                "(p.data > :dataLimiteInferior and p.data < :dataLimiteSuperior) or " +
                "(p.data = :dataLimiteInferior and p.horario >= :#{#dto.horario}) or " +
                "(p.data = :dataLimiteSuperior and p.horario <= :#{#dto.horario})" +
            ")")
    Boolean verificarDisponibilidade(@Param("dto") PartidaDTO dto,
                                     @Param("dataLimiteInferior") LocalDate dataLimiteInferior,
                                     @Param("dataLimiteSuperior") LocalDate dataLimiteSuperior);

}
