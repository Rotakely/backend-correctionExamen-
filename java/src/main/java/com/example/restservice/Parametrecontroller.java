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

import com.entity.Matiere;
import com.entity.Operateur;
import com.entity.Parametre;
import com.entity.Resolution;
import com.service.ParametreService;  

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class Parametrecontroller {

    @Autowired
    private ParametreService parametreService;  
    
    @Autowired
    private com.repository.OperateurRepository operateurRepository;
    
    @Autowired
    private com.repository.MatiereRepository matiereRepository;
    
    @Autowired
    private com.repository.ResolutionRepository resolutionRepository;

    @GetMapping("/operateur")
    public List<Operateur> getAllOperateurs() {
        return operateurRepository.findAll();
    }
    
    @GetMapping("/matieres-pour-parametre")
    public List<Matiere> getAllMatieres() {
        return matiereRepository.findAll();
    }
    
    @GetMapping("/resolution")
    public List<Resolution> getAllResolutions() {
        return resolutionRepository.findAll();
    }
    
    @PostMapping("/parametres")
    public Map<String, Object> ajouterParametre(@RequestBody Map<String, Object> payload) {
        
        Integer operateurId = Integer.parseInt(payload.get("operateurId").toString());
        Integer matiereId = Integer.parseInt(payload.get("matiereId").toString());
        Integer resolutionId = Integer.parseInt(payload.get("resolutionId").toString());
        BigDecimal diff = new BigDecimal(payload.get("diff").toString());
        
        Operateur operateur = operateurRepository.findById(operateurId)
            .orElseThrow(() -> new RuntimeException("Opérateur non trouvé"));
        
        Matiere matiere = matiereRepository.findById(matiereId)
            .orElseThrow(() -> new RuntimeException("Matière non trouvée"));
        
        Resolution resolution = resolutionRepository.findById(resolutionId)
            .orElseThrow(() -> new RuntimeException("Résolution non trouvée"));
        
        Parametre parametre = new Parametre();
        parametre.setOperateur(operateur);
        parametre.setMatiere(matiere);
        parametre.setResolution(resolution);
        parametre.setDiff(diff);
        
        // ✅ Utilisation du service
        Parametre savedParametre = parametreService.createParametre(parametre);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", savedParametre.getId());
        response.put("diff", savedParametre.getDiff());
        
        Map<String, Object> operateurInfo = new HashMap<>();
        operateurInfo.put("id", operateur.getId());
        operateurInfo.put("operateur", operateur.getOperateur());
        response.put("operateur", operateurInfo);
        
        Map<String, Object> matiereInfo = new HashMap<>();
        matiereInfo.put("id", matiere.getId());
        matiereInfo.put("nom", matiere.getNom());
        response.put("matiere", matiereInfo);
        
        Map<String, Object> resolutionInfo = new HashMap<>();
        resolutionInfo.put("id", resolution.getId());
        resolutionInfo.put("resolution", resolution.getResolution());
        response.put("resolution", resolutionInfo);
        
        return response;
    }
    
    @GetMapping("/parametres")
    public List<Map<String, Object>> getAllParametres() {
        // ✅ Utilisation du service
        List<Parametre> parametres = parametreService.getAllParametres();
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Parametre p : parametres) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", p.getId());
            map.put("diff", p.getDiff());
            
            if (p.getOperateur() != null) {
                Map<String, Object> opMap = new HashMap<>();
                opMap.put("id", p.getOperateur().getId());
                opMap.put("operateur", p.getOperateur().getOperateur());
                map.put("operateur", opMap);
            }
            
            if (p.getMatiere() != null) {
                Map<String, Object> matMap = new HashMap<>();
                matMap.put("id", p.getMatiere().getId());
                matMap.put("nom", p.getMatiere().getNom());
                map.put("matiere", matMap);
            }
            
            if (p.getResolution() != null) {
                Map<String, Object> resMap = new HashMap<>();
                resMap.put("id", p.getResolution().getId());
                resMap.put("resolution", p.getResolution().getResolution());
                map.put("resolution", resMap);
            }
            
            result.add(map);
        }
        
        return result;
    }
    
    @GetMapping("/parametres/{id}")
    public Map<String, Object> getParametreById(@PathVariable Integer id) {
        // ✅ Utilisation du service
        Parametre p = parametreService.getParametreById(id)
            .orElseThrow(() -> new RuntimeException("Paramètre non trouvé avec l'id: " + id));
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", p.getId());
        response.put("diff", p.getDiff());
        
        if (p.getOperateur() != null) {
            Map<String, Object> opMap = new HashMap<>();
            opMap.put("id", p.getOperateur().getId());
            opMap.put("operateur", p.getOperateur().getOperateur());
            response.put("operateur", opMap);
        }
        
        if (p.getMatiere() != null) {
            Map<String, Object> matMap = new HashMap<>();
            matMap.put("id", p.getMatiere().getId());
            matMap.put("nom", p.getMatiere().getNom());
            response.put("matiere", matMap);
        }
        
        if (p.getResolution() != null) {
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("id", p.getResolution().getId());
            resMap.put("resolution", p.getResolution().getResolution());
            response.put("resolution", resMap);
        }
        
        return response;
    }

    @PutMapping("/parametres/{id}")
    public Map<String, Object> updateParametre(@PathVariable Integer id, @RequestBody Map<String, Object> payload) {
        
        // Récupérer le paramètre existant
        Parametre existingParametre = parametreService.getParametreById(id)
            .orElseThrow(() -> new RuntimeException("Paramètre non trouvé avec l'id: " + id));
        
        // Récupérer les nouvelles valeurs
        Integer operateurId = Integer.parseInt(payload.get("operateurId").toString());
        Integer matiereId = Integer.parseInt(payload.get("matiereId").toString());
        Integer resolutionId = Integer.parseInt(payload.get("resolutionId").toString());
        BigDecimal diff = new BigDecimal(payload.get("diff").toString());
        
        // Récupérer les entités
        Operateur operateur = operateurRepository.findById(operateurId)
            .orElseThrow(() -> new RuntimeException("Opérateur non trouvé"));
        
        Matiere matiere = matiereRepository.findById(matiereId)
            .orElseThrow(() -> new RuntimeException("Matière non trouvée"));
        
        Resolution resolution = resolutionRepository.findById(resolutionId)
            .orElseThrow(() -> new RuntimeException("Résolution non trouvée"));
        
        // Mettre à jour
        existingParametre.setOperateur(operateur);
        existingParametre.setMatiere(matiere);
        existingParametre.setResolution(resolution);
        existingParametre.setDiff(diff);
        
        // ✅ Utilisation du service pour update
        Parametre updatedParametre = parametreService.updateParametre(existingParametre);
        
        // Préparer la réponse
        Map<String, Object> response = new HashMap<>();
        response.put("id", updatedParametre.getId());
        response.put("diff", updatedParametre.getDiff());
        
        Map<String, Object> operateurInfo = new HashMap<>();
        operateurInfo.put("id", operateur.getId());
        operateurInfo.put("operateur", operateur.getOperateur());
        response.put("operateur", operateurInfo);
        
        Map<String, Object> matiereInfo = new HashMap<>();
        matiereInfo.put("id", matiere.getId());
        matiereInfo.put("nom", matiere.getNom());
        response.put("matiere", matiereInfo);
        
        Map<String, Object> resolutionInfo = new HashMap<>();
        resolutionInfo.put("id", resolution.getId());
        resolutionInfo.put("resolution", resolution.getResolution());
        response.put("resolution", resolutionInfo);
        
        return response;
    }

    @DeleteMapping("/parametres/{id}")
    public Map<String, String> deleteParametre(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        
        // ✅ Utilisation du service
        boolean deleted = parametreService.deleteParametre(id);
        
        if (deleted) {
            response.put("message", "Paramètre supprimé avec succès");
            response.put("id", id.toString());
        } else {
            response.put("message", "Paramètre non trouvé");
        }
        
        return response;
    }
}