package com.waynesouza.soccer.config;

import com.waynesouza.soccer.domain.Equipe;
import com.waynesouza.soccer.domain.Estadio;
import com.waynesouza.soccer.domain.Partida;
import com.waynesouza.soccer.domain.dto.PartidaDTO;
import com.waynesouza.soccer.service.EquipeService;
import com.waynesouza.soccer.service.EstadioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ModelMapperConfig {

    private final EquipeService equipeService;
    private final EstadioService estadioService;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        configurePartidaDTOToPartida(modelMapper);
        configurePartidaToPartidaDTO(modelMapper);

        return modelMapper;
    }

    private void configurePartidaDTOToPartida(ModelMapper modelMapper) {
        Converter<String, Equipe> converterParaEquipe = conv -> equipeService.buscarOuCriar(conv.getSource());
        Converter<String, Estadio> converterParaEstadio = conv -> estadioService.buscarOuCriar(conv.getSource());

        modelMapper.addMappings(new PropertyMap<PartidaDTO, Partida>() {
            @Override
            protected void configure() {
                using(converterParaEquipe).map(source.getEquipeMandante(), destination.getEquipeMandante());
                using(converterParaEquipe).map(source.getEquipeVisitante(), destination.getEquipeVisitante());
                using(converterParaEstadio).map(source.getEstadio(), destination.getEstadio());
            }
        });
    }

    private void configurePartidaToPartidaDTO(ModelMapper modelMapper) {
        modelMapper.addMappings(new PropertyMap<Partida, PartidaDTO>() {
            @Override
            protected void configure() {
                map().setEquipeMandante(source.getEquipeMandante().getNome());
                map().setEquipeVisitante(source.getEquipeVisitante().getNome());
                map().setEstadio(source.getEstadio().getNome());
            }
        });
    }

}
