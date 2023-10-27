package com.waynesouza.soccer.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name = "time_mandante_id")
    private Time timeMandante;

    @Column(name = "qtd_gol_mandante")
    private Integer golsMandante;

    @ManyToOne
    @JoinColumn(name = "time_visitante_id")
    private Time timeVisitante;

    @Column(name = "qtd_gol_visitante")
    private Integer golsVisitante;

    private LocalDate data;

    private LocalTime horario;

    @ManyToOne
    @JoinColumn(name = "estadio_id")
    private Estadio estadio;

}
