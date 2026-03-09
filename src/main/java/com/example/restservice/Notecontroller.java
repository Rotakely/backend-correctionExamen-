package com.example.restservice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entity.Correcteur;
import com.entity.Etudiant;
import com.entity.Matiere;
import com.entity.Note;
import com.service.NoteService;  // ← IMPORT DU SERVICE

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class Notecontroller {

    @Autowired
    private NoteService noteService;  // ← UTILISATION DU SERVICE
    
    @Autowired
    private com.repository.EtudiantRepository etudiantRepository;
    
    @Autowired
    private com.repository.MatiereRepository matiereRepository;
    
    @Autowired
    private com.repository.CorrecteurRepository correcteurRepository;

    @GetMapping("/etudiants")
    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }
    
    @GetMapping("/matieres")
    public List<Matiere> getAllMatieres() {
        return matiereRepository.findAll();
    }
    
    @GetMapping("/correcteurs")
    public List<Correcteur> getAllCorrecteurs() {
        return correcteurRepository.findAll();
    }
    
    @PostMapping("/notes")
    public Map<String, Object> ajouterNote(@RequestBody Map<String, Object> payload) {
        
        Integer etudiantId = Integer.parseInt(payload.get("etudiantId").toString());
        Integer matiereId = Integer.parseInt(payload.get("matiereId").toString());
        Integer correcteurId = Integer.parseInt(payload.get("correcteurId").toString());
        BigDecimal noteValue = new BigDecimal(payload.get("note").toString());
        
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
            .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        
        Matiere matiere = matiereRepository.findById(matiereId)
            .orElseThrow(() -> new RuntimeException("Matière non trouvée"));
        
        Correcteur correcteur = correcteurRepository.findById(correcteurId)
            .orElseThrow(() -> new RuntimeException("Correcteur non trouvé"));
        
        Note note = new Note();
        note.setEtudiant(etudiant);
        note.setMatiere(matiere);
        note.setCorrecteur(correcteur);
        note.setNote(noteValue);
        
        // ✅ Utilisation du service
        Note savedNote = noteService.createNote(note);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", savedNote.getId());
        response.put("note", savedNote.getNote());
        
        Map<String, Object> etudiantInfo = new HashMap<>();
        etudiantInfo.put("id", etudiant.getId());
        etudiantInfo.put("nom", etudiant.getNom());
        response.put("etudiant", etudiantInfo);
        
        Map<String, Object> matiereInfo = new HashMap<>();
        matiereInfo.put("id", matiere.getId());
        matiereInfo.put("nom", matiere.getNom());
        response.put("matiere", matiereInfo);
        
        Map<String, Object> correcteurInfo = new HashMap<>();
        correcteurInfo.put("id", correcteur.getId());
        correcteurInfo.put("nom", correcteur.getNom());
        response.put("correcteur", correcteurInfo);
        
        return response;
    }
    
    @GetMapping("/notes")
    public List<Map<String, Object>> getAllNotes() {
        // ✅ Utilisation du service
        List<Note> notes = noteService.getAllNotes();
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Note note : notes) {
            Map<String, Object> noteMap = new HashMap<>();
            noteMap.put("id", note.getId());
            noteMap.put("note", note.getNote());
            
            if (note.getEtudiant() != null) {
                Map<String, Object> etudiantMap = new HashMap<>();
                etudiantMap.put("id", note.getEtudiant().getId());
                etudiantMap.put("nom", note.getEtudiant().getNom());
                noteMap.put("etudiant", etudiantMap);
            }
            
            if (note.getMatiere() != null) {
                Map<String, Object> matiereMap = new HashMap<>();
                matiereMap.put("id", note.getMatiere().getId());
                matiereMap.put("nom", note.getMatiere().getNom());
                noteMap.put("matiere", matiereMap);
            }
            
            if (note.getCorrecteur() != null) {
                Map<String, Object> correcteurMap = new HashMap<>();
                correcteurMap.put("id", note.getCorrecteur().getId());
                correcteurMap.put("nom", note.getCorrecteur().getNom());
                noteMap.put("correcteur", correcteurMap);
            }
            
            result.add(noteMap);
        }
        
        return result;
    }
    
    @GetMapping("/notes/{id}")
    public Map<String, Object> getNoteById(@PathVariable Integer id) {
        // ✅ Utilisation du service
        Note note = noteService.getNoteById(id)
            .orElseThrow(() -> new RuntimeException("Note non trouvée avec l'id: " + id));
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", note.getId());
        response.put("note", note.getNote());
        
        if (note.getEtudiant() != null) {
            Map<String, Object> etudiantMap = new HashMap<>();
            etudiantMap.put("id", note.getEtudiant().getId());
            etudiantMap.put("nom", note.getEtudiant().getNom());
            response.put("etudiant", etudiantMap);
        }
        
        if (note.getMatiere() != null) {
            Map<String, Object> matiereMap = new HashMap<>();
            matiereMap.put("id", note.getMatiere().getId());
            matiereMap.put("nom", note.getMatiere().getNom());
            response.put("matiere", matiereMap);
        }
        
        if (note.getCorrecteur() != null) {
            Map<String, Object> correcteurMap = new HashMap<>();
            correcteurMap.put("id", note.getCorrecteur().getId());
            correcteurMap.put("nom", note.getCorrecteur().getNom());
            response.put("correcteur", correcteurMap);
        }
        
        return response;
    }
    
    @PutMapping("/notes/{id}")
    public Map<String, Object> updateNote(@PathVariable Integer id, @RequestBody Map<String, Object> payload) {
        
        // Récupérer la note existante via le service
        Note existingNote = noteService.getNoteById(id)
            .orElseThrow(() -> new RuntimeException("Note non trouvée avec l'id: " + id));
        
        Integer etudiantId = Integer.parseInt(payload.get("etudiantId").toString());
        Integer matiereId = Integer.parseInt(payload.get("matiereId").toString());
        Integer correcteurId = Integer.parseInt(payload.get("correcteurId").toString());
        BigDecimal noteValue = new BigDecimal(payload.get("note").toString());
        
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
            .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        
        Matiere matiere = matiereRepository.findById(matiereId)
            .orElseThrow(() -> new RuntimeException("Matière non trouvée"));
        
        Correcteur correcteur = correcteurRepository.findById(correcteurId)
            .orElseThrow(() -> new RuntimeException("Correcteur non trouvé"));
        
        existingNote.setEtudiant(etudiant);
        existingNote.setMatiere(matiere);
        existingNote.setCorrecteur(correcteur);
        existingNote.setNote(noteValue);
        
        // ✅ Utilisation du service pour update
        Note updatedNote = noteService.updateNote(existingNote);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", updatedNote.getId());
        response.put("note", updatedNote.getNote());
        
        Map<String, Object> etudiantInfo = new HashMap<>();
        etudiantInfo.put("id", etudiant.getId());
        etudiantInfo.put("nom", etudiant.getNom());
        response.put("etudiant", etudiantInfo);
        
        Map<String, Object> matiereInfo = new HashMap<>();
        matiereInfo.put("id", matiere.getId());
        matiereInfo.put("nom", matiere.getNom());
        response.put("matiere", matiereInfo);
        
        Map<String, Object> correcteurInfo = new HashMap<>();
        correcteurInfo.put("id", correcteur.getId());
        correcteurInfo.put("nom", correcteur.getNom());
        response.put("correcteur", correcteurInfo);
        
        return response;
    }
    
    @DeleteMapping("/notes/{id}")
    public Map<String, String> deleteNote(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        
        // ✅ Utilisation du service
        boolean deleted = noteService.deleteNote(id);
        
        if (deleted) {
            response.put("message", "Note supprimée avec succès");
            response.put("id", id.toString());
        } else {
            response.put("message", "Note non trouvée");
        }
        
        return response;
    }
    
    @GetMapping("/etudiants/{etudiantId}/notes")
    public List<Note> getNotesByEtudiant(@PathVariable Integer etudiantId) {
        return noteService.getNotesByEtudiant(etudiantId);
    }
    
    @GetMapping("/matieres/{matiereId}/notes")
    public List<Note> getNotesByMatiere(@PathVariable Integer matiereId) {
        return noteService.getNotesByMatiere(matiereId);
    }
}