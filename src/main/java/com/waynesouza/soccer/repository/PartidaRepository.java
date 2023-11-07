package com.waynesouza.soccer.repository;

import com.waynesouza.soccer.domain.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, UUID> {

    Integer countPartidasByEstadioAndData(String estadio, LocalDate data);

    @Query("select count(p) from Partida p " +
            "where (p.timeMandante in (:times) or p.timeVisitante in (:times)) " +
            "and (" +
            "(p.data > :dataLimiteInferior and p.data < :dataLimiteSuperior) or " +
            "(p.data = :dataLimiteInferior and p.horario >= :horario) or " +
            "(p.data = :dataLimiteSuperior and p.horario <= :horario)" +
            ")")
    Integer countPartidasByTimesAndData(@Param("times") List<String> times,
                                        @Param("dataLimiteInferior") LocalDate dataLimiteInferior,
                                        @Param("dataLimiteSuperior") LocalDate dataLimiteSuperior,
                                        @Param("horario") LocalTime horario);

}
