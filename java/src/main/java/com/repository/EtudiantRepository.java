package com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.Etudiant;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Integer> {
    // Correction: findByNom retourne Optional<Etudiant> ou List<Etudiant>
    Optional<Etudiant> findByNom(String nom);
    
    // Pour rechercher par partie du nom (utile)
    List<Etudiant> findByNomContainingIgnoreCase(String nom);
}

