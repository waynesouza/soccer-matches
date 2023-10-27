package com.waynesouza.soccer.repository;

import com.waynesouza.soccer.domain.Estadio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EstadioRepository extends JpaRepository<Estadio, UUID> {

    Optional<Estadio> findByNome(String nome);

}
