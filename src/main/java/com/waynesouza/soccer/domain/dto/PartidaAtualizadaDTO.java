package com.waynesouza.soccer.domain.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private BigDecimal quantidadeGolVisitante;

    private LocalDateTime dataHora;

    private String estadio;

}
