const API_URL = "http://localhost:8080/api/client";

// 1. LIRE TOUT (READ ALL) - Pour afficher le tableau
export const getAllClients = async () => {
    const response = await fetch(API_URL);
    return await response.json();
};

// 2. LIRE UN SEUL (READ ONE) - Utile pour charger les données dans un formulaire de modif
export const getClientById = async (id) => {
    const response = await fetch(`${API_URL}/${id}`);
    return await response.json();
};


// 3. CRÉER (CREATE) - Pour le formulaire d'ajout
export const createClient = async (clientData) => {
    const response = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(clientData)
    });
    return await response.json();
};


// 4. MODIFIER (UPDATE) - Pour enregistrer les changements
export const updateClient = async (id, clientData) => {
    const response = await fetch(`${API_URL}/${id}`, {
        method: "PUT", // <--- On utilise PUT pour la modification
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(clientData)
    });
    return await response.json();
};

// 5. SUPPRIMER (DELETE) - Pour le bouton poubelle
export const deleteClient = async (id) => {
    await fetch(`${API_URL}/${id}`, { 
        method: "DELETE" 
    });
};

