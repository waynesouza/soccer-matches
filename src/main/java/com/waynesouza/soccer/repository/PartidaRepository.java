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

//    @Query("select p from Partida p where p.")
//    Partida countPartidasByTimesAndData(String timeMandante, String timeVisitante, LocalDate data, LocalTime horario);

}
