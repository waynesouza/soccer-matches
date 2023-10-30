package com.waynesouza.soccer.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Table(name = "partida")
@Entity
@Getter
@Setter
public class Partida implements Serializable {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "time_mandante", nullable = false)
    private String timeMandante;

    @Column(name = "quantidade_gol_mandante", nullable = false)
    private Integer quantidadeGolMandante;

    @Column(name = "time_visitante", nullable = false)
    private String timeVisitante;

    @Column(name = "quantidade_gol_visitante", nullable = false)
    private Integer quantidadeGolVisitante;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime horario;

    @Column(nullable = false)
    private String estadio;

}
