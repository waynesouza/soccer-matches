package com.waynesouza.soccer.domain;

import com.waynesouza.soccer.domain.enumeration.EnumResultado;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe_mandante_id", nullable = false)
    private Equipe equipeMandante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe_visitante_id", nullable = false)
    private Equipe equipeVisitante;

    @Column(name = "quantidade_gol_mandante", nullable = false)
    private Integer quantidadeGolMandante;

    @Column(name = "quantidade_gol_visitante", nullable = false)
    private Integer quantidadeGolVisitante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumResultado resultado;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estadio_id", nullable = false)
    private Estadio estadio;

}
