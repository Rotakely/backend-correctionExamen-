import React, { useState, useEffect } from 'react';
// On importe maintenant updatedemande
import { getAllDemande, createDemande, deleteDemande, updateDemande } from '../service/DemandeService';
import { getAllClients } from '../service/ClientService';

export default function DemandeForm() {
    const [demande, setdemande] = useState([]);
    const [form, setForm] = useState({ idclient: "", lieu: "" ,date:"",district:""});
    const [editId, setEditId] = useState(null); // Savoir si on est en mode "Ajout" ou "Modif"
    const [clients, setClients] = useState([]); // Pour stocker les clients du SELECT

    useEffect(() => { charger(); }, []);

    const charger = async () => {
        const data = await getAllDemande();
        setdemande(data);

        const dataClients = await getAllClients();
        setClients(dataClients)
    };

    // --- MODE ÉDITION : On remplit le formulaire avec les infos existantes ---
    const preparerModif = (d) => {
        setEditId(d.id);
        setForm({ 
            lieu: d.lieu, 
            district: d.district, 
            date: d.date,
            // On récupère l'ID à l'intérieur de l'objet client
            idclient: d.client ? d.client.id : "" 
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
    
        // 1. On extrait 'idclient' de 'form' et on met tout le reste dans 'resteDuForm'
        const { idclient, ...resteDuForm } = form;
    
        // 2. On reconstruit l'objet propre pour Java
        const donneesEnvoyees = { 
            ...resteDuForm, // lieu, date, district sont ici automatiquement
            client: { id: parseInt(idclient) } // On transforme l'id extrait en objet
        };
    
        console.log("Objet envoyé à l'API :", donneesEnvoyees);
    
        if (editId) {
            await updateDemande(editId, donneesEnvoyees);
            setEditId(null);
        } else {
            await createDemande(donneesEnvoyees);
        }
    
        setForm({ idclient: "", lieu: "", date: "", district: "" });
        charger();
    };
    return (
        <div style={{ padding: '20px' }}>
            <h2>{editId ? "Modifier le demande" : "Ajouter un demande"}</h2>
            
            <form onSubmit={handleSubmit}>
                <input 
                    placeholder="lieu" 
                    value={form.lieu} 
                    onChange={e => setForm({...form, lieu: e.target.value})} 
                    required
                />
                <input 
                    placeholder="district" 
                    value={form.district} 
                    onChange={e => setForm({...form, district: e.target.value})} 
                    required
                />

                <input 
                    type="date"             // <--- C'est ça qui crée le calendrier
                    placeholder="date" 
                    value={form.date} 
                    onChange={e => setForm({...form, date: e.target.value})} 
                    required
                    style={{ margin: '5px', padding: '5px' }} // Un peu d'espace
                />

                <select 
                    value={form.idclient} 
                    // Quand on choisit un nom, on enregistre son ID dans le formulaire
                    onChange={e => setForm({...form, idclient: e.target.value})} 
                    required
                    style={{ padding: '5px', margin: '5px' }}
                >
                    <option value="">-- Sélectionner un Client --</option>
                    
                    {/* On fait une boucle sur la mémoire 'clients' */}
                    {clients.map(cli => (
                        <option key={cli.id} value={cli.id}>
                            {cli.nom} (ID: {cli.id})
                        </option>
                    ))}
                </select>
                <button type="submit" style={{ backgroundColor: editId ? 'orange' : 'green', color: 'white' }}>
                    {editId ? "Mettre à jour" : "Enregistrer"}
                </button>
                
                {editId && (
                <button onClick={() => { 
                    setEditId(null); 
                    setForm({ idclient: "", lieu: "", date: "", district: "" }); // On vide TOUT
                }}>
                    Annuler
                </button>
            )}
            </form>

            <hr />

            <h3>Liste des demande</h3>
            <table border="1" width="100%">
                <thead>
                    <tr>
                        <th>lieu</th>
                        <th>district</th>
                       <th>date</th>
                       <th>idclient</th>
                        <th>Actions</th>
                        
                    </tr>
                </thead>
                <tbody>
    {demande.map(c => (
        <tr key={c.id}>
            <td>{c.lieu}</td>
            <td>{c.district}</td>
            <td>{c.date}</td>
            {/* ICI : On affiche l'ID du client qui est DANS l'objet client */}
            <td>{c.client ? c.client.nom : "Inconnu"}</td>
            <td>
                <button onClick={() => preparerModif(c)}>Modifier</button>
                <button onClick={async () => { if(window.confirm("Supprimer ?")) { await deleteDemande(c.id); charger(); } }} style={{color: 'red'}}>
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

