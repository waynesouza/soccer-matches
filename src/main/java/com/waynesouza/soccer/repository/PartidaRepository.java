package com.waynesouza.soccer.repository;

import com.waynesouza.soccer.domain.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, UUID> { }
