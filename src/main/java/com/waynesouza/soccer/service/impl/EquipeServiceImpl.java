package com.waynesouza.soccer.service.impl;

import com.waynesouza.soccer.domain.Equipe;
import com.waynesouza.soccer.repository.EquipeRepository;
import com.waynesouza.soccer.service.EquipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EquipeServiceImpl implements EquipeService {

    private final EquipeRepository repository;

    @Override
    public Equipe buscarOuCriar(String nome) {
        Equipe equipe = repository.findByNome(nome);

        if (Objects.isNull(equipe)) {
            return repository.save(new Equipe(nome));
        }

        return equipe;
    }
}
