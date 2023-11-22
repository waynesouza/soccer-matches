package com.waynesouza.soccer.repository;

import com.waynesouza.soccer.domain.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquipeRepository extends JpaRepository<Equipe, String> {

    Equipe findByNome(String nome);

}
