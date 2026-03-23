package com.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entity.Correcteur;
import com.repository.CorrecteurRepository;

@Service
@Transactional
public class CorrecteurService {

    @Autowired
    private CorrecteurRepository correcteurRepository;

    public Integer createCorrecteur(Correcteur correcteur) {
        Correcteur saved = correcteurRepository.save(correcteur);
        return saved.getId();
    }

    public boolean deleteCorrecteur(Integer id) {
        if (!correcteurRepository.existsById(id)) return false;
        correcteurRepository.deleteById(id);
        return true;
    }

    public boolean updateCorrecteur(Correcteur correcteur) {
        if (!correcteurRepository.existsById(correcteur.getId())) return false;
        correcteurRepository.save(correcteur);
        return true;
    }

    public List<Correcteur> getAllCorrecteurs() {
        return correcteurRepository.findAll();
    }

    public Optional<Correcteur> getCorrecteurById(Integer id) {
        return correcteurRepository.findById(id);
    }
    
    
    public Optional<Correcteur> getCorrecteurByNom(String nom) {
        return correcteurRepository.findByNom(nom);
    }
}