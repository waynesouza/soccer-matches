package com.waynesouza.soccer.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class FreguesDTO implements Serializable {

    private String time;
    private Long totalPartidas;
    private Long vitorias;
    private Long derrotas;

}
