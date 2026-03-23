const API_URL = "http://localhost:8080/api/Demande";

// 1. LIRE TOUT (READ ALL) - Pour afficher le tableau
export const getAllDemande = async () => {
    const response = await fetch(API_URL);
    return await response.json();
};

// 2. LIRE UN SEUL (READ ONE) - Utile pour charger les données dans un formulaire de modif
export const getDemandeById = async (id) => {
    const response = await fetch(`${API_URL}/${id}`);
    return await response.json();
};


// 3. CRÉER (CREATE) - Pour le formulaire d'ajout
export const createDemande = async (demande) => {
    const response = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(demande)
    });
    return await response.json();
};


// 4. MODIFIER (UPDATE) - Pour enregistrer les changements
export const updateDemande = async (id, demande) => {
    const response = await fetch(`${API_URL}/${id}`, {
        method: "PUT", // <--- On utilise PUT pour la modification
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(demande)
    });
    return await response.json();
};

// 5. SUPPRIMER (DELETE) - Pour le bouton poubelle
export const deleteDemande = async (id) => {
    await fetch(`${API_URL}/${id}`, { 
        method: "DELETE" 
    });
};

