package com.waynesouza.soccer.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RetrospectoClubeDTO implements Serializable {

    private Long vitorias;
    private Long empates;
    private Long derrotas;
    private Long golsPro;
    private Long golsSofrido;

}
