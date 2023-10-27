package com.waynesouza.soccer.service.impl;

import com.waynesouza.soccer.domain.Time;
import com.waynesouza.soccer.repository.TimeRepository;
import com.waynesouza.soccer.service.TimeService;
import com.waynesouza.soccer.service.dto.TimeDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TimeServiceImpl implements TimeService {

    private final ModelMapper mapper;
    private final TimeRepository repository;

    @Override
    public TimeDTO buscarOuSalvar(String nome) {
        Time timeExistente = repository.findByNome(nome).orElse(null);
        if (Objects.isNull(timeExistente)) {
            Time time = repository.save(mapper.map(TimeDTO.builder().nome(nome).build(), Time.class));
            return mapper.map(time, TimeDTO.class);
        }
        return mapper.map(timeExistente, TimeDTO.class);
    }

}
