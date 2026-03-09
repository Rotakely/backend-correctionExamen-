package com.example.restservice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entity.Etudiant;
import com.entity.Matiere;
import com.service.NoteService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private NoteService noteService;
    
    @Autowired
    private com.repository.EtudiantRepository etudiantRepository;
    
    @Autowired
    private com.repository.MatiereRepository matiereRepository;
    
    @PostMapping("/mijeryNoteFinale")
    public Map<String, Object> mijeryNoteFinale(@RequestBody Map<String, Integer> request) {
        
        Integer etudiantId = request.get("etudiantId");
        Integer matiereId = request.get("matiereId");
        
        if (etudiantId == null || matiereId == null) {
            throw new RuntimeException("etudiantId et matiereId sont requis");
        }
        
        // Récupérer l'étudiant et la matière
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
            .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        
        Matiere matiere = matiereRepository.findById(matiereId)
            .orElseThrow(() -> new RuntimeException("Matière non trouvée"));
        
        // Calculer la note finale
        BigDecimal noteFinale = noteService.calculerNoteFinale(etudiantId, matiereId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("nom", etudiant.getNom());
        response.put("matiere", matiere.getNom());     // ← "matiere" au lieu de "Matiere"
        response.put("note", noteFinale);               // ← "note" au lieu de "Note" atao mitov any react afihcier note finale
        return response;
        
        
    }
}