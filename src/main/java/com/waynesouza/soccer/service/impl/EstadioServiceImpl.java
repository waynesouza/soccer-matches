package com.waynesouza.soccer.service.impl;

import com.waynesouza.soccer.domain.Estadio;
import com.waynesouza.soccer.repository.EstadioRepository;
import com.waynesouza.soccer.service.EstadioService;
import com.waynesouza.soccer.service.dto.EstadioDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EstadioServiceImpl implements EstadioService {

    private final ModelMapper mapper;
    private final EstadioRepository repository;

    @Override
    public Estadio buscarOuSalvar(String nome) {
        Estadio estadioExistente = repository.findByNome(nome).orElse(null);
        if (Objects.isNull(estadioExistente)) {
            EstadioDTO estadioDTO = new EstadioDTO();
            estadioDTO.setNome(nome);
            return repository.save(mapper.map(estadioDTO, Estadio.class));
        }
        return estadioExistente;
    }

}
