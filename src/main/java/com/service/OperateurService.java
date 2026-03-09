package com.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entity.Operateur;
import com.repository.OperateurRepository;

@Service
@Transactional
public class OperateurService {

    @Autowired
    private OperateurRepository operateurRepository;

    public Integer createOperateur(Operateur operateur) {
        Operateur saved = operateurRepository.save(operateur);
        return saved.getId();
    }

    public boolean deleteOperateur(Integer id) {
        if (!operateurRepository.existsById(id)) return false;
        operateurRepository.deleteById(id);
        return true;
    }

    public boolean updateOperateur(Operateur operateur) {
        if (!operateurRepository.existsById(operateur.getId())) return false;
        operateurRepository.save(operateur);
        return true;
    }

    public List<Operateur> getAllOperateurs() {
        return operateurRepository.findAll();
    }

    public Optional<Operateur> getOperateurById(Integer id) {
        return operateurRepository.findById(id);
    }
    
    public Optional<Operateur> getOperateurBySymbole(String symbole) {
        return operateurRepository.findByOperateur(symbole);
    }
}