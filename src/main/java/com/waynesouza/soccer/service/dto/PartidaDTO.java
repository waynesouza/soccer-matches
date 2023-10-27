package com.waynesouza.soccer.service.dto;

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
    private String timeMandante;
    private Integer golsMandante;
    private String timeVisitante;
    private Integer golsVisitante;
    private LocalDate data;
    private LocalTime horario;
    private String estadio;


}
