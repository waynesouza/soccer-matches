package com.waynesouza.soccer.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;

@Table(name = "estadio")
@Entity
@Getter
@Setter
public class Estadio implements Serializable {

    @Id
    @UuidGenerator
    private String id;

    @Column(nullable = false)
    private String nome;

    public Estadio() {}

    public Estadio(String nome) {
        this.nome = nome;
    }

}
