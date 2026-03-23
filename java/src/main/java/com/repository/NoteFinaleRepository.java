package com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.Etudiant;
import com.entity.NoteFinale;

@Repository
public interface NoteFinaleRepository extends JpaRepository<NoteFinale, Integer> {
    Optional<NoteFinale> findByEtudiant(Etudiant etudiant);
    Optional<NoteFinale> findByEtudiantId(Integer etudiantId);
}