package com.waynesouza.soccer.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class RetrospectoConfrontoDTO implements Serializable {

    private Long vitoriasPrimeiroTime;
    private Long vitoriasSegundoTime;
    private Long empates;
    private Long golsPrimeiroTime;
    private Long golsSegundoTime;

}
