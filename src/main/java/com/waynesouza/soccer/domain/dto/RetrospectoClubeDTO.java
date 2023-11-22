package com.waynesouza.soccer.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class RetrospectoClubeDTO implements Serializable {

    private Long vitorias;
    private Long empates;
    private Long derrotas;
    private Long golsPro;
    private Long golsSofrido;

}
