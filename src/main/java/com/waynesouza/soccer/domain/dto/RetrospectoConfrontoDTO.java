package com.waynesouza.soccer.domain.dto;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class RetrospectoConfrontoDTO implements Serializable {

    private Long vitoriasPrimeiraEquipe;
    private Long vitoriasSegundaEquipe;
    private Long empates;
    private Long golsPrimeiraEquipe;
    private Long golsSegundaEquipe;

    public RetrospectoConfrontoDTO(Long vitoriasPrimeiraEquipe, Long vitoriasSegundaEquipe, Long empates, Long golsPrimeiraEquipe, Long golsSegundaEquipe) {
        this.vitoriasPrimeiraEquipe = verificarNulo(vitoriasPrimeiraEquipe);
        this.vitoriasSegundaEquipe = verificarNulo(vitoriasSegundaEquipe);
        this.empates = verificarNulo(empates);
        this.golsPrimeiraEquipe = verificarNulo(golsPrimeiraEquipe);
        this.golsSegundaEquipe = verificarNulo(golsSegundaEquipe);
    }

    private static Long verificarNulo(Long campo) {
        return campo != null ? campo : 0L;
    }

}
