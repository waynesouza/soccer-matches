package com.waynesouza.soccer.service.impl;

import com.waynesouza.soccer.domain.Estadio;
import com.waynesouza.soccer.repository.EstadioRepository;
import com.waynesouza.soccer.service.EstadioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EstadioServiceImpl implements EstadioService {

    private final EstadioRepository repository;

    @Override
    public Estadio buscarOuCriar(String nome) {
        Estadio estadio = repository.findByNome(nome);

        if (Objects.isNull(estadio)) {
            return repository.save(new Estadio(nome));
        }

        return estadio;
    }
}
