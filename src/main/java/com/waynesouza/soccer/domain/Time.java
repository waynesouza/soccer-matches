package com.waynesouza.soccer.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Table(name = "time")
@Entity
@Getter
@Setter
public class Time implements Serializable {

    @Id
    @UuidGenerator
    private UUID id;

    private String nome;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "timeMandante")
    private List<Partida> partidasComoMandante;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "timeVisitante")
    private List<Partida> partidasComoVisitante;

}
