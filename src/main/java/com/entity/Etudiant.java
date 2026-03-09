package com.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "etudiant")
public class Etudiant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "nom", nullable = false, length = 50)
    private String nom;

    // Relation One-to-Many avec Note
    @OneToMany(mappedBy = "etudiant", fetch = FetchType.LAZY)
      @JsonIgnore  // ← AJOUTE ÇA !
    private List<Note> notes;

    // Relation One-to-One avec NoteFinale
    @OneToOne(mappedBy = "etudiant", fetch = FetchType.LAZY)
    @JsonIgnore  // ← AJOUTE ÇA !
    private NoteFinale noteFinale;

    // Constructeurs
    public Etudiant() {}

    public Etudiant(String nom) {
        this.nom = nom;
    }

    // Getters & Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public NoteFinale getNoteFinale() {
        return noteFinale;
    }

    public void setNoteFinale(NoteFinale noteFinale) {
        this.noteFinale = noteFinale;
    }
}