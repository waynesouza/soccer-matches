package com.waynesouza.soccer.repository;

import com.waynesouza.soccer.domain.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, UUID> {

    Integer countPartidasByEstadioAndData(String estadio, LocalDate data);

    @Query("select count(p) from Partida p " +
            "where (p.timeMandante in (:timeMandante, :timeVisitante) " +
            "or p.timeVisitante in (:timeMandante, :timeVisitante)) " +
            "and ((:data - p.data = 2 and :horario > p.horario) or (:data - p.data > 2)) ")
    Integer countPartidasByTimesAndData(String timeMandante, String timeVisitante, LocalDate data, LocalTime horario);

}
