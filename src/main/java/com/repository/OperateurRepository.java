package com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.Operateur;

@Repository
public interface OperateurRepository extends JpaRepository<Operateur, Integer> {
    Optional<Operateur> findByOperateur(String operateur);
}