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
    public Time buscarOuSalvar(String nome) {
        Time timeExistente = repository.findByNome(nome).orElse(null);
        if (Objects.isNull(timeExistente)) {
            TimeDTO timeDTO = new TimeDTO();
            timeDTO.setNome(nome);
            return repository.save(mapper.map(timeDTO, Time.class));
        }
        return timeExistente;
    }

}
