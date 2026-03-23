package com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.Matiere;

@Repository
public interface MatiereRepository extends JpaRepository<Matiere, Integer> {
    Optional<Matiere> findByNom(String nom);
    List<Matiere> findByCoeff(Integer coeff);
}