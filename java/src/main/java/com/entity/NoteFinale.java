package com.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "notefinale")
public class NoteFinale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_candididat", referencedColumnName = "id")
    private Etudiant etudiant;
    
    @Column(name = "notefinale", precision = 10, scale = 2)
    private BigDecimal noteFinale;

    // Constructeurs
    public NoteFinale() {}

    public NoteFinale(Etudiant etudiant, BigDecimal noteFinale) {
        this.etudiant = etudiant;
        this.noteFinale = noteFinale;
    }

    // Getters & Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public BigDecimal getNoteFinale() {
        return noteFinale;
    }

    public void setNoteFinale(BigDecimal noteFinale) {
        this.noteFinale = noteFinale;
    }
}