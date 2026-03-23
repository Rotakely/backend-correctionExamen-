package com.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entity.Resolution;
import com.repository.ResolutionRepository;

@Service
@Transactional
public class ResolutionService {

    @Autowired
    private ResolutionRepository resolutionRepository;

    public Integer createResolution(Resolution resolution) {
        Resolution saved = resolutionRepository.save(resolution);
        return saved.getId();
    }

    public boolean deleteResolution(Integer id) {
        if (!resolutionRepository.existsById(id)) return false;
        resolutionRepository.deleteById(id);
        return true;
    }

    public boolean updateResolution(Resolution resolution) {
        if (!resolutionRepository.existsById(resolution.getId())) return false;
        resolutionRepository.save(resolution);
        return true;
    }

    public List<Resolution> getAllResolutions() {
        return resolutionRepository.findAll();
    }

    public Optional<Resolution> getResolutionById(Integer id) {
        return resolutionRepository.findById(id);
    }
    
    public Optional<Resolution> getResolutionByNom(String nom) {
        return resolutionRepository.findByResolution(nom);
    }
}