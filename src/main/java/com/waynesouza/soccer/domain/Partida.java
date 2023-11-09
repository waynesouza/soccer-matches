package com.waynesouza.soccer.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;

@Table(name = "partida")
@Entity
@Getter
@Setter
public class Partida implements Serializable {

    @Id
    @UuidGenerator
    private String id;

    @Column(name = "time_mandante", nullable = false)
    private String timeMandante;

    @Column(name = "quantidade_gol_mandante", nullable = false)
    private Integer quantidadeGolMandante;

    @Column(name = "time_visitante", nullable = false)
    private String timeVisitante;

    @Column(name = "quantidade_gol_visitante", nullable = false)
    private Integer quantidadeGolVisitante;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private String estadio;

}
