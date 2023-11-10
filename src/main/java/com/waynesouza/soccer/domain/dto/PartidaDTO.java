package com.waynesouza.soccer.domain.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PartidaDTO implements Serializable {

    private String id;

    @NotNull(message = "O time mandante deve ser informado")
    private String timeMandante;

    @NotNull(message = "A quantidade de gols do time mandante deve ser informada")
    @Min(value = 0, message = "O valor deve ser um número inteiro maior que zero")
    @Digits(integer = 2, fraction = 0, message = "O valor deve ser um número inteiro positivo ou zero")
    private BigDecimal quantidadeGolMandante;

    @NotNull(message = "O time visitante deve ser informado")
    private String timeVisitante;

    @NotNull(message = "A quantidade de gols do time visitante deve ser informada")
    @Min(value = 0, message = "O valor deve ser um número inteiro maior que zero")
    @Digits(integer = 2, fraction = 0, message = "O valor deve ser um número inteiro positivo ou zero")
    private Integer quantidadeGolVisitante;

    @NotNull(message = "A data com o horário da partida deve ser informada")
    private LocalDateTime dataHora;

    @NotNull(message = "O estádio da partida deve ser informado")
    private String estadio;


}
