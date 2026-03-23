package com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.Resolution;

@Repository
public interface ResolutionRepository extends JpaRepository<Resolution, Integer> {
    Optional<Resolution> findByResolution(String resolution);
}