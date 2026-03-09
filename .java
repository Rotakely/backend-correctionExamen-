// resa service
/**
 * Traite les réservations en séparant les passagers si nécessaire
 * @param resa Liste des réservations à traiter
 * @param date Date de l'optimisation
 * @return Nombre de réservations traitées
 */
public int traiter_resa_avec_separation_passagers(List<Reservation> resa, Date date) throws Exception {
    try {
        if (affichageActif) {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("🔀 TRAITEMENT AVEC SÉPARATION DES PASSAGERS");
            System.out.println("=".repeat(70));
        }
        
        // 1. Trier par nombre de passagers décroissant
        List<Reservation> trie = tri_desc_resa_passager(resa);
        
        // 2. Liste pour suivre les réservations déjà traitées
        Set<Integer> traitees = new HashSet<>();
        int compteur = 0;
        
        for (Reservation r : trie) {
            if (traitees.contains(r.getId())) continue;
            
            if (affichageActif) {
                System.out.println("\n📋 Traitement réservation #" + r.getId() + 
                    " (" + r.getNom() + " " + r.getPrenom() + 
                    ") - " + r.getNbrPassager() + " passagers");
            }
            
            // Récupérer les voitures disponibles triées par nombre de places décroissant
            List<Vehicule> voituresDispo = vehiculeService.get_voitures_dispo(date, r.getHeureArrivee());
            List<Vehicule> voituresTriees = vehiculeService.tri_decroissant_voiture(voituresDispo);
            
            int passagersRestants = r.getNbrPassager();
            List<Vehicule> voituresUtilisees = new ArrayList<>();
            List<Integer> passagersParVoiture = new ArrayList<>();
            
            // Essayer de répartir les passagers dans plusieurs voitures
            for (Vehicule v : voituresTriees) {
                if (passagersRestants <= 0) break;
                
                if (v.getNb_place() >= passagersRestants) {
                    // Cette voiture peut prendre tous les passagers restants
                    voituresUtilisees.add(v);
                    passagersParVoiture.add(passagersRestants);
                    passagersRestants = 0;
                    break;
                } else {
                    // Cette voiture prend une partie des passagers
                    voituresUtilisees.add(v);
                    passagersParVoiture.add(v.getNb_place());
                    passagersRestants -= v.getNb_place();
                }
            }
            
            if (passagersRestants > 0) {
                // Impossible de répartir tous les passagers
                if (affichageActif) {
                    System.out.println("❌ Impossible de répartir les " + r.getNbrPassager() + 
                        " passagers - places disponibles insuffisantes");
                }
                continue;
            }
            
            // Maintenant, nous avons une liste de voitures pour cette réservation
            if (affichageActif) {
                System.out.println("✅ Répartition des " + r.getNbrPassager() + " passagers dans " + 
                    voituresUtilisees.size() + " véhicule(s) :");
            }
            
            // Pour chaque voiture, créer une "sous-réservation" virtuelle
            int cumulPassagers = 0;
            for (int i = 0; i < voituresUtilisees.size(); i++) {
                Vehicule v = voituresUtilisees.get(i);
                int passagersDansCetteVoiture = passagersParVoiture.get(i);
                
                if (affichageActif) {
                    System.out.println("  ➡️ " + passagersDansCetteVoiture + " passagers dans " + 
                        v.getImmatriculation() + " (" + v.getNb_place() + " places)");
                }
                
                // Créer un groupe d'une seule réservation (la même)
                List<Reservation> groupe = new ArrayList<>();
                groupe.add(r);
                
                // Insérer l'historique pour cette partie
                historiqueReservationService.insererGroupeReservationsAvecPassagersPartiels(
                    r, v, passagersDansCetteVoiture);
                
                // Mettre à jour les places restantes dans la voiture
                int restePlace = v.getNb_place() - passagersDansCetteVoiture;
                vehiculeService.update_place_voiture(v, restePlace);
                
                // Incrémenter le compteur de trajets (une seule fois par voiture)
                if (i == 0) {
                    vehiculeService.incrementTrajet(v);
                }
                
                cumulPassagers += passagersDansCetteVoiture;
            }
            
            // Marquer la réservation comme traitée
            traitees.add(r.getId());
            compteur++;
            
            if (affichageActif) {
                System.out.println("✅ Réservation #" + r.getId() + " traitée avec succès");
            }
        }
        
        if (affichageActif) {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("✅ FIN DU TRAITEMENT - " + compteur + " réservation(s) traitée(s)");
            System.out.println("=".repeat(70) + "\n");
        }
        
        return compteur;
        
    } catch (Exception e) {
        throw new Exception("Erreur dans traiter_resa_avec_separation_passagers: " + e.getMessage(), e);
    }
}

// historique resa service
/**
 * Insère un historique pour une réservation avec un nombre partiel de passagers
 * @param reservation La réservation d'origine
 * @param vehicule Le véhicule utilisé
 * @param nbPassagersDansCeVehicule Nombre de passagers dans ce véhicule
 */
public void insererGroupeReservationsAvecPassagersPartiels(
        Reservation reservation, 
        Vehicule vehicule,
        int nbPassagersDansCeVehicule) throws Exception {
    
    try {
        int restePlace = vehicule.getNb_place() - nbPassagersDansCeVehicule;
        
        // Calculer l'heure de retour pour cette partie
        List<Reservation> listeSimple = new ArrayList<>();
        listeSimple.add(reservation);
        double vitesse = parametrageService.getVitesseMoyenne();
        Time heureRetour = calculerHeureRetourGroupe(listeSimple, vitesse);
        
        // Créer l'historique
        Historique_reservation historique = new Historique_reservation(
            reservation, 
            vehicule, 
            restePlace, 
            heureRetour
        );
        
        Historique_reservation historiqueInsere = createHistoriqueReservation(historique);
        
        if (affichageActif) {
            System.out.println("    ✓ " + nbPassagersDansCeVehicule + " passagers placés dans " + 
                vehicule.getImmatriculation() + " (reste " + restePlace + " places)");
        }
        
        if (historiqueInsere.getId() == null) {
            throw new Exception("Erreur lors de l'insertion de l'historique partiel");
        }
        
    } catch (Exception e) {
        throw new Exception("Erreur insertion partielle: " + e.getMessage(), e);
    }
}

// resa service
public int traiter_resa_avec_max_passager(List<Reservation> resa, Date date) throws Exception {
    try {
        // 1. Trier par nombre de passagers décroissant
        List<Reservation> trie = tri_desc_resa_passager(resa);
        
        // 2. Liste pour suivre les réservations déjà traitées
        Set<Integer> traitees = new HashSet<>();
        int compteur = 0;
        
        for (int i = 0; i < trie.size(); i++) {
            Reservation r = trie.get(i);
            if (traitees.contains(r.getId())) continue;
            
            if (affichageActif) {
                System.out.println("\n📋 Traitement réservation #" + r.getId() + 
                    " (" + r.getNom() + " " + r.getPrenom() + ") - " + 
                    r.getNbrPassager() + " passagers");
            }
            
            // 3. Chercher une voiture pour cette réservation
            Vehicule v = vehiculeService.selectionnerMeilleureVoiture(
                    r.getNbrPassager(), date, r.getHeureArrivee());
            
            if (v == null) {
                // ✅ NOUVEAU : Si pas de voiture, essayer de séparer les passagers
                if (affichageActif) {
                    System.out.println("  ⚠️ Aucune voiture unique trouvée, tentative de séparation des passagers...");
                }
                
                // Créer une liste temporaire avec cette seule réservation
                List<Reservation> listeSimple = new ArrayList<>();
                listeSimple.add(r);
                
                // Utiliser la nouvelle fonction de séparation
                int resultat = traiter_resa_avec_separation_passagers(listeSimple, date);
                if (resultat > 0) {
                    traitees.add(r.getId());
                    compteur++;
                }
                continue;
            }
            
            // 4. Créer un groupe pour cette voiture
            List<Reservation> groupe = new ArrayList<>();
            groupe.add(r);
            traitees.add(r.getId());
            
            // 5. Places restantes
            int reste = v.getNb_place() - r.getNbrPassager();
            
            // 6. Chercher d'autres réservations à ajouter dans cette même voiture
            if (reste > 0) {
                for (Reservation autre : trie) {
                    if (!traitees.contains(autre.getId()) && 
                        autre.getNbrPassager() <= reste) {
                        
                        groupe.add(autre);
                        traitees.add(autre.getId());
                        reste -= autre.getNbrPassager();
                        
                        if (affichageActif) {
                            System.out.println("  + Ajout #" + autre.getId() + 
                                " (" + autre.getNbrPassager() + " pass) dans " + 
                                v.getImmatriculation());
                        }
                        
                        if (reste <= 0) break;
                    }
                }
            }
            
            // 7. Insérer TOUT le groupe d'un coup
            if (!groupe.isEmpty()) {
                // Trier le groupe par heure d'arrivée pour l'ordre de visite
                groupe.sort((r1, r2) -> r1.getHeureArrivee().compareTo(r2.getHeureArrivee()));
                
                // Insérer le groupe
                historiqueReservationService.insererGroupeReservations(groupe, v);
                
                // Incrémenter le nombre de trajets du véhicule (une seule fois)
                vehiculeService.incrementTrajet(v);
                
                compteur += groupe.size();
                
                if (affichageActif) {
                    System.out.println("✅ Groupe de " + groupe.size() + 
                        " réservations inséré dans " + v.getImmatriculation());
                }
            }
        }
        
        return compteur;
        
    } catch (Exception e) {
        throw new Exception("Erreur dans traiter_resa_avec_max_passager: " + e.getMessage(), e);
    }
}

// cas 2
public int cas_2(Reservation reservation_v1, Date date, Set<Integer> reservations_deja_traitees) throws Exception {
    try {
        afficher_entete_cas2();
        afficher_info_reservation(reservation_v1);

        // Récupérer toutes les réservations dans le temps d'attente
        List<Reservation> reservations_attente_brut = get_reservations_attente_avec_resa1(reservation_v1, date);
        
        // Filtrer les réservations déjà traitées
        List<Reservation> reservations_attente = filtrerReservationsNonTraitees(reservations_attente_brut, reservations_deja_traitees);

        // Calculer le nombre total de passagers
        int nbr_passager_total = somme_passager(reservations_attente);
        
        // Récupérer les voitures disponibles
        List<Vehicule> voitures_dispo = vehiculeService.get_voitures_dispo(date, reservation_v1.getHeureArrivee());
        
        // Chercher une voiture pour tout le monde
        Vehicule voiture_hanatitra = vehiculeService.selectionnerMeilleureVoiture(
            nbr_passager_total, date, reservation_v1.getHeureArrivee());

        if (voiture_hanatitra != null) {
            return traiterAvecUneVoiturePourTous(reservations_attente, voiture_hanatitra);
        } else {
            // ✅ MODIFIÉ : Utiliser la nouvelle fonction avec séparation
            if (affichageActif) {
                System.out.println("\n⚠️ Aucune voiture unique pour tous les passagers");
                System.out.println("   Tentative d'optimisation avec séparation si nécessaire...\n");
            }
            return traiter_resa_avec_max_passager(reservations_attente, date);
        }
    
    } catch (Exception e) {
        throw e;
    }
}

// vehicule service
/**
 * Met à jour les places restantes d'un véhicule
 */
public void update_place_voiture(Vehicule v, int reste_place) {
    if (v != null) {
        v.setNb_place(reste_place);
        vehiculeRepository.save(v);
    }
}