package com.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entity.Etudiant;
import com.repository.EtudiantRepository;

@Service
@Transactional
public class EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    public Integer createEtudiant(Etudiant etudiant) {
        Etudiant saved = etudiantRepository.save(etudiant);
        return saved.getId();
    }

    public boolean deleteEtudiant(Integer id) {
        if (!etudiantRepository.existsById(id)) return false;
        etudiantRepository.deleteById(id);
        return true;
    }

    public boolean updateEtudiant(Etudiant etudiant) {
        if (!etudiantRepository.existsById(etudiant.getId())) return false;
        etudiantRepository.save(etudiant);
        return true;
    }

    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }

    public Optional<Etudiant> getEtudiantById(Integer id) {
        return etudiantRepository.findById(id);
    }
    
    public Optional<Etudiant> getEtudiantByNom(String nom) {
        return etudiantRepository.findByNom(nom);
    }
}