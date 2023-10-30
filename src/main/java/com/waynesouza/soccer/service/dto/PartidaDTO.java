package com.waynesouza.soccer.service.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
public class PartidaDTO implements Serializable {

    private UUID id;

    @NotNull
    private String timeMandante;

    @NotNull
    @PositiveOrZero
    @Digits(integer = 2, fraction = 0)
    private Integer quantidadeGolMandante;

    @NotNull
    private String timeVisitante;

    @NotNull
    @PositiveOrZero
    @Digits(integer = 2, fraction = 0)
    private Integer quantidadeGolVisitante;

    @NotNull
    private LocalDate data;

    @NotNull
    private LocalTime horario;

    @NotNull
    private String estadio;


}
