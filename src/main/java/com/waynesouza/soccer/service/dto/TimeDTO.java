package com.waynesouza.soccer.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TimeDTO implements Serializable {

    private UUID id;
    private String nome;

}
