package com.waynesouza.soccer.repository;

import com.waynesouza.soccer.domain.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TimeRepository extends JpaRepository<Time, UUID> { }
