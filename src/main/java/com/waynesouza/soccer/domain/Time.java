package com.waynesouza.soccer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Table(name = "time")
@Entity
@Getter
@Setter
public class Time implements Serializable {

    @Id
    private UUID id;

    private String nome;

    @OneToMany(mappedBy = "timeMandante")
    private List<Partida> partidasComoMandante;

    @OneToMany(mappedBy = "timeVisitante")
    private List<Partida> partidasComoVisitante;

}
