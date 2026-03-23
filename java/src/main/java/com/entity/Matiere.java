package com.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name = "matiere")
public class Matiere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "nom", nullable = false, length = 50)
    private String nom;
    
    @Column(name = "coeff")
    private Integer coeff;

    // Constructeurs
    public Matiere() {}

    public Matiere(String nom, Integer coeff) {
        this.nom = nom;
        this.coeff = coeff;
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

    public Integer getCoeff() {
        return coeff;
    }

    public void setCoeff(Integer coeff) {
        this.coeff = coeff;
    }
}