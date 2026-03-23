package com.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entity.Etudiant;
import com.entity.NoteFinale;
import com.repository.EtudiantRepository;
import com.repository.NoteFinaleRepository;

@Service
@Transactional
public class NoteFinaleService {

    @Autowired
    private NoteFinaleRepository noteFinaleRepository;
    
    @Autowired
    private EtudiantRepository etudiantRepository;

    public Integer createNoteFinale(NoteFinale noteFinale) {
        // Vérifier que l'étudiant existe
        if (noteFinale.getEtudiant() != null && noteFinale.getEtudiant().getId() != null) {
            Etudiant etudiant = etudiantRepository.findById(noteFinale.getEtudiant().getId())
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
            noteFinale.setEtudiant(etudiant);
        }
        
        NoteFinale saved = noteFinaleRepository.save(noteFinale);
        return saved.getId();
    }

    public boolean deleteNoteFinale(Integer id) {
        if (!noteFinaleRepository.existsById(id)) return false;
        noteFinaleRepository.deleteById(id);
        return true;
    }

    public boolean updateNoteFinale(NoteFinale noteFinale) {
        if (!noteFinaleRepository.existsById(noteFinale.getId())) return false;
        noteFinaleRepository.save(noteFinale);
        return true;
    }

    public List<NoteFinale> getAllNotesFinales() {
        return noteFinaleRepository.findAll();
    }

    public Optional<NoteFinale> getNoteFinaleById(Integer id) {
        return noteFinaleRepository.findById(id);
    }
    
    public Optional<NoteFinale> getNoteFinaleByEtudiant(Integer etudiantId) {
        return noteFinaleRepository.findByEtudiantId(etudiantId);
    }
}

