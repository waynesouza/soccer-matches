package com.waynesouza.soccer.service.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class PartidaAtualizadaDTO implements Serializable {

    private String timeMandante;

    @Min(value = 0, message = "O valor deve ser um número inteiro maior que zero")
    @Digits(integer = 2, fraction = 0, message = "O valor deve ser um número inteiro positivo ou zero")
    private BigDecimal quantidadeGolMandante;

    private String timeVisitante;

    @Min(value = 0, message = "O valor deve ser um número inteiro maior que zero")
    @Digits(integer = 2, fraction = 0, message = "O valor deve ser um número inteiro positivo ou zero")
    private Integer quantidadeGolVisitante;

    private LocalDate data;

    private LocalTime horario;

    private String estadio;

}
