package com.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entity.Matiere;
import com.repository.MatiereRepository;

@Service
@Transactional
public class MatiereService {

    @Autowired
    private MatiereRepository matiereRepository;

    public Integer createMatiere(Matiere matiere) {
        Matiere saved = matiereRepository.save(matiere);
        return saved.getId();
    }

    public boolean deleteMatiere(Integer id) {
        if (!matiereRepository.existsById(id)) return false;
        matiereRepository.deleteById(id);
        return true;
    }

    public boolean updateMatiere(Matiere matiere) {
        if (!matiereRepository.existsById(matiere.getId())) return false;
        matiereRepository.save(matiere);
        return true;
    }

    public List<Matiere> getAllMatieres() {
        return matiereRepository.findAll();
    }

    public Optional<Matiere> getMatiereById(Integer id) {
        return matiereRepository.findById(id);
    }
    
    public Optional<Matiere> getMatiereByNom(String nom) {
        return matiereRepository.findByNom(nom);
    }
}