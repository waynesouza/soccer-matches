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

@Table(name = "estadio")
@Entity
@Getter
@Setter
public class Estadio implements Serializable {

    @Id
    private UUID id;

    private String nome;

    @OneToMany(mappedBy = "estadio")
    private List<Partida> partidas;

}
