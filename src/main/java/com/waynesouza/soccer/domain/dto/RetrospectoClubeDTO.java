package com.waynesouza.soccer.domain.dto;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class RetrospectoClubeDTO implements Serializable {

    private Long vitorias;
    private Long empates;
    private Long derrotas;
    private Long golsPro;
    private Long golsSofrido;

    public RetrospectoClubeDTO(Long vitorias, Long empates, Long derrotas, Long golsPro, Long golsSofrido) {
        this.vitorias = verificarNulo(vitorias);
        this.empates = verificarNulo(empates);
        this.derrotas = verificarNulo(derrotas);
        this.golsPro = verificarNulo(golsPro);
        this.golsSofrido = verificarNulo(golsSofrido);
    }

    private static Long verificarNulo(Long campo) {
        return campo != null ? campo : 0L;
    }
}
