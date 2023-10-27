package com.waynesouza.soccer.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
public class EstadioDTO implements Serializable {

    private UUID id;
    private String nome;

}
