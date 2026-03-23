import React, { useState, useEffect } from 'react';
// On importe maintenant updateClient
import { getAllClients, createClient, deleteClient, updateClient } from '../service/ClientService';

export default function ClientForm() {
    const [clients, setClients] = useState([]);
    const [form, setForm] = useState({ nom: "", contact: "" });
    const [editId, setEditId] = useState(null); // Savoir si on est en mode "Ajout" ou "Modif"

    useEffect(() => { charger(); }, []);

    const charger = async () => {
        const data = await getAllClients();
        setClients(data);
    };

    // --- MODE ÉDITION : On remplit le formulaire avec les infos existantes ---
    const preparerModif = (client) => {
        setEditId(client.id); // On stocke l'ID à modifier
        setForm({ nom: client.nom, contact: client.contact }); // On affiche dans l'input
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (editId) {
            // Si editId existe, on fait un PUT (Update)
            await updateClient(editId, form);
            setEditId(null); // On sort du mode édition
        } else {
            // Sinon, on fait un POST (Create)
            await createClient(form);
        }

        setForm({ nom: "", contact: "" }); // Vider les champs
        charger(); // Recharger la liste
    };

    return (
        <div style={{ padding: '20px' }}>
            <h2>{editId ? "Modifier le Client" : "Ajouter un Client"}</h2>
            
            <form onSubmit={handleSubmit}>
                <input 
                    placeholder="Nom" 
                    value={form.nom} 
                    onChange={e => setForm({...form, nom: e.target.value})} 
                    required
                />
                <input 
                    placeholder="Contact" 
                    value={form.contact} 
                    onChange={e => setForm({...form, contact: e.target.value})} 
                    required
                />
                <button type="submit" style={{ backgroundColor: editId ? 'orange' : 'green', color: 'white' }}>
                    {editId ? "Mettre à jour" : "Enregistrer"}
                </button>
                
                {editId && <button onClick={() => {setEditId(null); setForm({nom:"", contact:""})}}>Annuler</button>}
            </form>

            <hr />

            <h3>Liste des Clients</h3>
            <table border="1" width="100%">
                <thead>
                    <tr>
                        <th>Nom</th>
                        <th>Contact</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {clients.map(c => (
                        <tr key={c.id}>
                            <td>{c.nom}</td>
                            <td>{c.contact}</td>
                            <td>
                                {/* BOUTON MODIFIER */}
                                <button onClick={() => preparerModif(c)}>Modifier</button>
                                
                                {/* BOUTON SUPPRIMER */}
                                <button onClick={async () => { if(window.confirm("Supprimer ?")) { await deleteClient(c.id); charger(); } }} style={{color: 'red'}}>
                                    X
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

