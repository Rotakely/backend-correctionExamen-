package com.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entity.Matiere;
import com.entity.Operateur;
import com.entity.Parametre;
import com.entity.Resolution;
import com.repository.MatiereRepository;
import com.repository.OperateurRepository;
import com.repository.ParametreRepository;
import com.repository.ResolutionRepository;

@Service
@Transactional
public class ParametreService {

    @Autowired
    private ParametreRepository parametreRepository;
    
    @Autowired
    private MatiereRepository matiereRepository;
    
    @Autowired
    private OperateurRepository operateurRepository;
    
    @Autowired
    private ResolutionRepository resolutionRepository;

    // ========== CREATE ==========
    public Parametre createParametre(Parametre parametre) {
        // Vérifier que les entités associées existent
        if (parametre.getMatiere() != null && parametre.getMatiere().getId() != null) {
            Matiere matiere = matiereRepository.findById(parametre.getMatiere().getId())
                .orElseThrow(() -> new RuntimeException("Matière non trouvée"));
            parametre.setMatiere(matiere);
        }
        
        if (parametre.getOperateur() != null && parametre.getOperateur().getId() != null) {
            Operateur operateur = operateurRepository.findById(parametre.getOperateur().getId())
                .orElseThrow(() -> new RuntimeException("Opérateur non trouvé"));
            parametre.setOperateur(operateur);
        }
        
        if (parametre.getResolution() != null && parametre.getResolution().getId() != null) {
            Resolution resolution = resolutionRepository.findById(parametre.getResolution().getId())
                .orElseThrow(() -> new RuntimeException("Résolution non trouvée"));
            parametre.setResolution(resolution);
        }
        
        return parametreRepository.save(parametre);  // Retourne l'objet, pas seulement l'ID
    }

    // ========== READ ALL ==========
    public List<Parametre> getAllParametres() {
        return parametreRepository.findAll();
    }

    // ========== READ ONE ==========
    public Optional<Parametre> getParametreById(Integer id) {
        return parametreRepository.findById(id);
    }

    // ========== UPDATE ==========
    public Parametre updateParametre(Parametre parametre) {
        if (!parametreRepository.existsById(parametre.getId())) {
            throw new RuntimeException("Paramètre non trouvé avec l'id: " + parametre.getId());
        }
        return parametreRepository.save(parametre);  // Retourne l'objet mis à jour
    }

    // ========== DELETE ==========
    public boolean deleteParametre(Integer id) {
        if (parametreRepository.existsById(id)) {
            parametreRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ========== RECHERCHES SPÉCIFIQUES ==========
    public List<Parametre> getParametresByMatiere(Integer matiereId) {
        Matiere matiere = matiereRepository.findById(matiereId)
            .orElseThrow(() -> new RuntimeException("Matière non trouvée"));
        return parametreRepository.findByMatiere(matiere);
    }
    
    public List<Parametre> getParametresByOperateur(Integer operateurId) {
        Operateur operateur = operateurRepository.findById(operateurId)
            .orElseThrow(() -> new RuntimeException("Opérateur non trouvé"));
        return parametreRepository.findByOperateur(operateur);
    }
    
    public List<Parametre> getParametresByResolution(Integer resolutionId) {
        Resolution resolution = resolutionRepository.findById(resolutionId)
            .orElseThrow(() -> new RuntimeException("Résolution non trouvée"));
        return parametreRepository.findByResolution(resolution);
    }
}