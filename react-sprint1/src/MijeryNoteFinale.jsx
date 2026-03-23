import { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "./AuthContext";
import "./MijeryNoteFinale.css";  // ← IMPORT DU CSS

export default function MijeryNoteFinale() {
  const [data, setData] = useState({ etudiantId: "", matiereId: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const { setAnkizy } = useContext(AuthContext);
  const navigate = useNavigate();

  function handleChange(e) {
    setData({ ...data, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);
    setError("");

    if (!data.etudiantId || !data.matiereId) {
      setError("Veuillez remplir tous les champs");
      setLoading(false);
      return;
    }

    const payload = {
      etudiantId: parseInt(data.etudiantId),
      matiereId: parseInt(data.matiereId)
    };

    try {
      const response = await fetch("http://localhost:8080/api/auth/mijeryNoteFinale", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const errorText = await response.text();
        setError(`Erreur ${response.status}: ${errorText || "MijeryNoteFinale incorrect"}`);
        setLoading(false);
        return;
      }

      const ankizyData = await response.json();

      if (!ankizyData) {
        setError("Données reçues vides");
        setLoading(false);
        return;
      }

      setAnkizy(ankizyData);
      navigate("/notefinale");

    } catch (error) {
      setError("Erreur de connexion au serveur");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="mijery-container">  {/* ← NOUVEAU */}
      <h2 className="mijery-title">Consultation de note</h2>  {/* ← NOUVEAU */}
      
      {error && <div className="mijery-error">{error}</div>}  {/* ← NOUVEAU */}

      <form onSubmit={handleSubmit} className="mijery-form">  {/* ← NOUVEAU */}
        <div className="mijery-form-group">  {/* ← NOUVEAU */}
          <label className="mijery-label">Identifiant étudiant</label>  {/* ← NOUVEAU */}
          <input 
            name="etudiantId"
            type="number" 
            placeholder="ex: 1" 
            value={data.etudiantId}
            onChange={handleChange} 
            required
            className="mijery-input"  
          />
        </div>
        
        <div className="mijery-form-group">  {/* ← NOUVEAU */}
          <label className="mijery-label">Identifiant matière</label>  {/* ← NOUVEAU */}
          <input 
            name="matiereId"
            type="number" 
            placeholder="ex: 2" 
            value={data.matiereId}
            onChange={handleChange} 
            required
            className="mijery-input" 
          />
        </div>
        
        <button 
          type="submit" 
          disabled={loading}
          className="mijery-button"  
        >
          {loading ? (
            <>
              <span className="mijery-loading"></span>
              Consultation...
            </>
          ) : "🌸 Voir la note"}
        </button>
      </form>
      
      <div className="mijery-decoration"> 
        <span>✨</span> Entre tes identifiants pour découvrir ta note <span>✨</span>
      </div>
    </div>
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

