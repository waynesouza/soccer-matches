package com.waynesouza.soccer.domain.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PartidaAtualizadaDTO implements Serializable {

    private String equipeMandante;

    @Min(value = 0, message = "O valor deve ser um número inteiro maior que zero")
    @Digits(integer = 2, fraction = 0, message = "O valor deve ser um número inteiro positivo ou zero")
    private BigDecimal quantidadeGolMandante;

    private String equipeVisitante;

    @Min(value = 0, message = "O valor deve ser um número inteiro maior que zero")
    @Digits(integer = 2, fraction = 0, message = "O valor deve ser um número inteiro positivo ou zero")
    private Integer quantidadeGolVisitante;

    private LocalDateTime dataHora;

    private String estadio;

}
