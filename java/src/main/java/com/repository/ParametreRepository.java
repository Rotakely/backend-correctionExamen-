package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.Matiere;
import com.entity.Operateur;
import com.entity.Parametre;
import com.entity.Resolution;

@Repository
public interface ParametreRepository extends JpaRepository<Parametre, Integer> {
    
    // Recherche par matière (objet)
    List<Parametre> findByMatiere(Matiere matiere);
    
    // Recherche par ID de matière
    List<Parametre> findByMatiereId(Integer matiereId);
    
    // Recherche par opérateur (objet)
    List<Parametre> findByOperateur(Operateur operateur);
    
    // Recherche par ID d'opérateur
    List<Parametre> findByOperateurId(Integer operateurId);
    
    // Recherche par résolution (objet)
    List<Parametre> findByResolution(Resolution resolution);
    
    // Recherche par ID de résolution
    List<Parametre> findByResolutionId(Integer resolutionId);
    
    // Recherche par matière ET opérateur
    List<Parametre> findByMatiereAndOperateur(Matiere matiere, Operateur operateur);
    
    // Recherche par matière ET résolution
    List<Parametre> findByMatiereAndResolution(Matiere matiere, Resolution resolution);
    
    // Recherche par opérateur ET résolution
    List<Parametre> findByOperateurAndResolution(Operateur operateur, Resolution resolution);
    
    // Recherche par valeur de diff (supérieure à)
    List<Parametre> findByDiffGreaterThan(Double diff);
    
    // Recherche par valeur de diff (inférieure à)
    List<Parametre> findByDiffLessThan(Double diff);
}