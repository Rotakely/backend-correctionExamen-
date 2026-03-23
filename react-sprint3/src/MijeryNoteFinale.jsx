import { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "./AuthContext";

export default function MijeryNoteFinale() {
  const [data, setData] = useState({ etudiantId: "", matiereId: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false); // Pour éviter les doubles soumissions

  const { setAnkizy } = useContext(AuthContext);
  const navigate = useNavigate();


  
  function handleChange(e) {
    setData({ ...data, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);
    setError("");

    // Validation
    if (!data.etudiantId || !data.matiereId) {
      setError("Veuillez remplir tous les champs");
      setLoading(false);
      return;
    }

    const payload = {
      etudiantId: parseInt(data.etudiantId),
      matiereId: parseInt(data.matiereId)
    };

    console.log("1. Envoi des données:", payload); // DEBUG

    try {
      const response = await fetch("http://localhost:8080/api/auth/mijeryNoteFinale", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });

      console.log("2. Statut de la réponse:", response.status); // DEBUG

      if (!response.ok) {
        const errorText = await response.text();
        console.log("3. Erreur retournée:", errorText); // DEBUG
        setError(`Erreur ${response.status}: ${errorText || "MijeryNoteFinale incorrect"}`);
        setLoading(false);
        return;
      }

      const ankizyData = await response.json();
      console.log("4. Données reçues du backend:", ankizyData); // DEBUG

      // Vérifiez que les données ne sont pas vides
      if (!ankizyData) {
        setError("Données reçues vides");
        setLoading(false);
        return;
      }

      setAnkizy(ankizyData);
      console.log("5. Données sauvegardées dans le contexte"); // DEBUG
      
      navigate("/notefinale");
      console.log("6. Navigation vers /notefinale"); // DEBUG

    } catch (error) {
      console.log("7. Erreur catch:", error); // DEBUG
      setError("Erreur de connexion au serveur");
    } finally {
      setLoading(false);
    }
  }

  return (
    <>
      <h2>MijeryNoteFinale</h2>
      {error && <p style={{color:"red"}}>{error}</p>}

      <form onSubmit={handleSubmit}>
        <input 
          name="etudiantId"
          type="number" 
          placeholder="ID étudiant" 
          value={data.etudiantId}
          onChange={handleChange} 
          required
        />
        <br />
        <input 
          name="matiereId"
          type="number" 
          placeholder="ID matière" 
          value={data.matiereId}
          onChange={handleChange} 
          required
        />
        <br />
        <button type="submit" disabled={loading}>
          {loading ? "Chargement..." : "voir"}
        </button>
      </form>
    </>
  );
}



// import { AuthContext } from "./AuthContext";

// export default function MijeryNoteFinale() {
//   const { setAnkizy } = useContext(AuthContext);  // ← ICI
//   // ...
  
//   async function handleSubmit() {
//     // ... appel API ...
//     const ankizy = await response.json();  // { nom: "Jean", Matiere: "Maths", Note: 15.5 }
//     setAnkizy(ankizy);  // ← ET ICI
//     navigate("/notefinale");
//   }
// }


// Ce que fait le contexte ici :
// useContext(AuthContext) : Le composant demande à avoir accès au tiroir AuthContext

// { setAnkizy } : Il récupère uniquement la fonction setAnkizy (pas besoin de lire ankizy)

// setAnkizy(ankizy) : Il met la nouvelle donnée dans le tiroir

// Résultat : La donnée { nom: "Jean", Matiere: "Maths", Note: 15.5 } est maintenant stockée dans le contexte ET dans localStorage.

