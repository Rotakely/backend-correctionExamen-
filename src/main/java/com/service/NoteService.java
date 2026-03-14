package com.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entity.Correcteur;
import com.entity.Etudiant;
import com.entity.Matiere;
import com.entity.Note;
import com.entity.Parametre;
import com.repository.CorrecteurRepository;
import com.repository.EtudiantRepository;
import com.repository.MatiereRepository;  // ← AJOUT (nécessaire pour la fonction calculerNoteFinale)
import com.repository.NoteRepository;
import com.repository.ParametreRepository;

@Service
@Transactional
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;
    
    @Autowired
    private EtudiantRepository etudiantRepository;
    
    @Autowired
    private MatiereRepository matiereRepository;
    
    @Autowired
    private CorrecteurRepository correcteurRepository;
    
    @Autowired
    private ParametreRepository parametreRepository;  // ← AJOUT (nécessaire pour la fonction calculerNoteFinale)

    // ========== CREATE ==========
    public Note createNote(Note note) {
        // Vérifier que les entités associées existent
        if (note.getEtudiant() != null && note.getEtudiant().getId() != null) {
            Etudiant etudiant = etudiantRepository.findById(note.getEtudiant().getId())
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
            note.setEtudiant(etudiant);
        }
        
        if (note.getMatiere() != null && note.getMatiere().getId() != null) {
            Matiere matiere = matiereRepository.findById(note.getMatiere().getId())
                .orElseThrow(() -> new RuntimeException("Matière non trouvée"));
            note.setMatiere(matiere);
        }
        
        if (note.getCorrecteur() != null && note.getCorrecteur().getId() != null) {
            Correcteur correcteur = correcteurRepository.findById(note.getCorrecteur().getId())
                .orElseThrow(() -> new RuntimeException("Correcteur non trouvé"));
            note.setCorrecteur(correcteur);
        }
        
        return noteRepository.save(note);
    }

    // ========== READ ALL ==========
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    // ========== READ ONE ==========
    public Optional<Note> getNoteById(Integer id) {
        return noteRepository.findById(id);
    }

    // ========== UPDATE ==========
    public Note updateNote(Note note) {
        if (!noteRepository.existsById(note.getId())) {
            throw new RuntimeException("Note non trouvée avec l'id: " + note.getId());
        }
        return noteRepository.save(note);
    }

    // ========== DELETE ==========
    public boolean deleteNote(Integer id) {
        if (noteRepository.existsById(id)) {
            noteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ========== RECHERCHES SPÉCIFIQUES ==========
    public List<Note> getNotesByEtudiant(Integer etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
            .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        return noteRepository.findByEtudiant(etudiant);
    }
    
    public List<Note> getNotesByMatiere(Integer matiereId) {
        Matiere matiere = matiereRepository.findById(matiereId)
            .orElseThrow(() -> new RuntimeException("Matière non trouvée"));
        return noteRepository.findByMatiere(matiere);
    }
    
    public List<Note> getNotesByCorrecteur(Integer correcteurId) {
        Correcteur correcteur = correcteurRepository.findById(correcteurId)
            .orElseThrow(() -> new RuntimeException("Correcteur non trouvé"));
        return noteRepository.findByCorrecteur(correcteur);
    }
    
    public List<Note> getNotesByEtudiantAndMatiere(Integer etudiantId, Integer matiereId) {
        return noteRepository.findByEtudiantIdAndMatiereId(etudiantId, matiereId);
    }

    // ============= VOS FONCTIONS (INCHANGÉES) =============
    
    /**
     * Fonction 1 : Vérifie si toutes les notes sont identiques
     */
    public boolean sontToutesIdentiques(List<Note> notes) {
        if (notes == null || notes.size() <= 1) return true;
        
        BigDecimal premiereNote = notes.get(0).getNote();
        for (int i = 1; i < notes.size(); i++) {
            if (notes.get(i).getNote().compareTo(premiereNote) != 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Fonction 2 : Calcule la somme des différences entre notes consécutives
     */
    public BigDecimal calculerSommeDifferences(List<Note> notes) {
        if (notes == null || notes.size() <= 1) {
            return BigDecimal.ZERO;
        }
    
        BigDecimal somme = BigDecimal.ZERO;
    
        for (int i = 0; i < notes.size() - 1; i++) {
            BigDecimal diff = notes.get(i + 1).getNote()
                    .subtract(notes.get(i).getNote())
                    .abs();
    
            System.out.println("Différence entre note " + i + " et " + (i+1) + " : " + diff);
    
            somme = somme.add(diff);
            System.out.println("Somme actuelle : " + somme);
        }
    
        System.out.println("Somme finale des différences : " + somme);
        return somme;
    }
    
    /**
     * Fonction 3 : Trouve la note maximale dans la liste
     */
    public BigDecimal trouverNoteMax(List<Note> notes) {
        return notes.stream()
            .map(Note::getNote)
            .max(BigDecimal::compareTo)
            .orElse(BigDecimal.ZERO);
    }
    
    /**
     * Fonction 4 : Trouve la note minimale dans la liste
     */
    public BigDecimal trouverNoteMin(List<Note> notes) {
        return notes.stream()
            .map(Note::getNote)
            .min(BigDecimal::compareTo)
            .orElse(BigDecimal.ZERO);
    }
    
    /**
     * Fonction 5 : Calcule la moyenne des notes
     */
    public BigDecimal calculerMoyenne(List<Note> notes) {
        BigDecimal somme = notes.stream()
            .map(Note::getNote)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return somme.divide(
            BigDecimal.valueOf(notes.size()), 
            2, 
            BigDecimal.ROUND_HALF_UP
        );
    }
    
    /**
     * Fonction 6 : Vérifie si une condition est vraie selon l'opérateur
     */
    // public boolean verifierCondition(BigDecimal sommeDifferences, BigDecimal diff, String operateur) {
    //     switch (operateur) {
    //         case ">": return sommeDifferences.compareTo(diff) > 0;
    //         case "<": return sommeDifferences.compareTo(diff) < 0;
    //         case "=": return sommeDifferences.compareTo(diff) == 0;
    //         case ">=": return sommeDifferences.compareTo(diff) >= 0;
    //         case "<=": return sommeDifferences.compareTo(diff) <= 0;

    //         default: throw new RuntimeException("Opérateur inconnu: " + operateur);
    //     }
    // }
    
    /**
     * Fonction 7 : Applique une résolution sur la liste de notes
     */
    public BigDecimal appliquerResolution(List<Note> notes, String resolution) {
        switch (resolution.toUpperCase()) {
            case "MAX":
                return trouverNoteMax(notes);
            case "MIN":
                return trouverNoteMin(notes);
            case "MOYENNE":
                return calculerMoyenne(notes);
            case "SOMME":
                return calculerSommeDifferences(notes);
            default:
                throw new RuntimeException("Résolution inconnue: " + resolution);
        }
    }
    



public BigDecimal trouverDiffLePlusProche(BigDecimal sommeDifferences, List<Parametre> parametres) {
    if (parametres == null || parametres.isEmpty()) {
        return null;
    }
    
    // Initialisation avec le premier paramètre
    BigDecimal plusProche = parametres.get(0).getDiff();
    BigDecimal minDistance = sommeDifferences.subtract(plusProche).abs();
    
    // Parcourir tous les paramètres
    for (int i = 1; i < parametres.size(); i++) {
        BigDecimal diffCourant = parametres.get(i).getDiff();
        BigDecimal distance = sommeDifferences.subtract(diffCourant).abs();
        
        // Comparaison des distances
        if (distance.compareTo(minDistance) < 0) {
            // Distance plus petite trouvée
            minDistance = distance;
            plusProche = diffCourant;
        }
        else if (distance.compareTo(minDistance) == 0) {
            // ÉGALITÉ: on prend le plus petit diff
            if (diffCourant.compareTo(plusProche) < 0) {
                plusProche = diffCourant;
            }
        }
    }
    
    return plusProche;
}
  

    /**
     * Fonction principale : Calcule la note finale avec les paramètres
     */
    // public BigDecimal calculerNoteFinale(Integer etudiantId, Integer matiereId) {
    //     // 1. Récupérer les notes
    //     List<Note> notes = noteRepository.findByEtudiantIdAndMatiereId(etudiantId, matiereId);
        
    //     if (notes.isEmpty()) {
    //         throw new RuntimeException("Aucune note trouvée");
    //     }
        
    //     // 2. Si une seule note, pas de calcul nécessaire
    //     if (notes.size() == 1) {
    //         return notes.get(0).getNote();
    //     }
        
    //     // 3. Vérifier si toutes les notes sont identiques
    //     if (sontToutesIdentiques(notes)) {
    //         return notes.get(0).getNote();
    //     }
        
    //     // 4. Calculer la somme des différences
    //     BigDecimal sommeDifferences = calculerSommeDifferences(notes);
        
    //     // 5. Récupérer les paramètres pour cette matière
    //     List<Parametre> parametres = parametreRepository.findByMatiereId(matiereId);
        
    //     if (parametres.isEmpty()) {
    //         // Pas de paramètres → retourner la somme des différences
    //         return sommeDifferences;
    //     }
        
    //     // 6. Appliquer les paramètres
    //     BigDecimal resultat = sommeDifferences;
        
    //     for (Parametre p : parametres) {
    //         String operateur = p.getOperateur().getOperateur();
    //         String resolution = p.getResolution().getResolution();
    //         BigDecimal diff = p.getDiff();
            
    //         if (verifierCondition(sommeDifferences, diff)) {
    //             resultat = appliquerResolution(notes, resolution);
    //             break; // On prend le premier paramètre qui satisfait la condition
    //         }
    //     }
        
    //     return resultat;
    // }

    /**
 * Fonction principale : Calcule la note finale avec les paramètres
 */
        public BigDecimal calculerNoteFinale(Integer etudiantId, Integer matiereId) {
            // 1. Récupérer les notes
            List<Note> notes = noteRepository.findByEtudiantIdAndMatiereId(etudiantId, matiereId);
            
            if (notes.isEmpty()) {
                throw new RuntimeException("Aucune note trouvée");
            }
            
            // 2. Si une seule note, pas de calcul nécessaire
            if (notes.size() == 1) {
                return notes.get(0).getNote();
            }
            
            // 3. Vérifier si toutes les notes sont identiques
            if (sontToutesIdentiques(notes)) {
                return notes.get(0).getNote();
            }
            
            // 4. Calculer la somme des différences
            BigDecimal sommeDifferences = calculerSommeDifferences(notes);
            
            // 5. Récupérer les paramètres pour cette matière
            List<Parametre> parametres = parametreRepository.findByMatiereId(matiereId);
            
            if (parametres.isEmpty()) {
                // Pas de paramètres → retourner la somme des différences
                return sommeDifferences;
            }
            
            // 6. NOUVEAU : Trouver le diff le plus proche de sommeDifferences
            BigDecimal diffLePlusProche = trouverDiffLePlusProche(sommeDifferences, parametres);
            
            // 7. Trouver le paramètre qui correspond à ce diff
            Parametre parametreChoisi = null;
            for (Parametre p : parametres) {
                if (p.getDiff().compareTo(diffLePlusProche) == 0) {
                    parametreChoisi = p;
                    break;
                }
            }
            
            // 8. Appliquer la résolution de ce paramètre
            String resolution = parametreChoisi.getResolution().getResolution();
            BigDecimal resultat = appliquerResolution(notes, resolution);
            
            return resultat;
        }


   
}

// mvn spring-boot:run