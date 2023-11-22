package com.waynesouza.soccer.repository;

import com.waynesouza.soccer.domain.Estadio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadioRepository extends JpaRepository<Estadio, String> {

    Estadio findByNome(String nome);

}
