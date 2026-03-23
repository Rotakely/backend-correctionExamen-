package com.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "note")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
      @JsonIgnore  
    @JoinColumn(name = "id_matiere", nullable = false)
    private Matiere matiere;
    
    @Column(name = "note", nullable = false, precision = 10, scale = 2)
    private BigDecimal note;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore  
    @JoinColumn(name = "id_correcteur", nullable = false)
    private Correcteur correcteur;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore  // ← AJOUTE ÇA !
    @JoinColumn(name = "id_etudiant", nullable = false)
    private Etudiant etudiant;

    // Constructeurs
    public Note() {}

    public Note(Matiere matiere, BigDecimal note, Correcteur correcteur, Etudiant etudiant) {
        this.matiere = matiere;
        this.note = note;
        this.correcteur = correcteur;
        this.etudiant = etudiant;
    }

    // Getters & Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public BigDecimal getNote() {
        return note;
    }

    public void setNote(BigDecimal note) {
        this.note = note;
    }

    public Correcteur getCorrecteur() {
        return correcteur;
    }

    public void setCorrecteur(Correcteur correcteur) {
        this.correcteur = correcteur;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }
}


// Pourquoi ça marche ?
// Sans @JsonIgnore : "Envoie l'étudiant → qui a des notes → qui ont des matières → qui ont des notes..." → Boucle infinie 💥

// Avec @JsonIgnore : "Envoie l'étudiant mais STOP, n'envoie pas ses notes" → Ça marche ✅