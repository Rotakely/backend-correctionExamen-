package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.Correcteur;
import com.entity.Etudiant;
import com.entity.Matiere;
import com.entity.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {
    List<Note> findByEtudiant(Etudiant etudiant);
    List<Note> findByEtudiantId(Integer etudiantId);
    List<Note> findByMatiere(Matiere matiere);
    List<Note> findByMatiereId(Integer matiereId);
    List<Note> findByCorrecteur(Correcteur correcteur);
    List<Note> findByCorrecteurId(Integer correcteurId);
    List<Note> findByEtudiantIdAndMatiereId(Integer etudiantId, Integer matiereId);
}