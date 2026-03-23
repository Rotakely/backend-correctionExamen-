import { useContext, useEffect } from "react";
import { AuthContext } from "./AuthContext";
import { useNavigate } from "react-router-dom";
import "./NoteFinale.css";  // ← IMPORT DU CSS

export default function NoteFinale() {
  const { ankizy, setAnkizy } = useContext(AuthContext);
  const navigate = useNavigate();

  useEffect(() => {
    console.log("NoteFinale monté");
    console.log("Contenu de ankizy:", ankizy);
    
    if (!ankizy) {
      console.log("Pas d'utilisateur dans le contexte");
    }
  }, [ankizy]);

  function logout() {
    setAnkizy(null);
    navigate("/");
  }

  function retourRecherche() {
    navigate("/");
  }

  if (!ankizy) {
    return (
      <div className="notefinale-container">
        <div className="notefinale-denied">
          <p>Accès refusé - Aucune donnée utilisateur</p>
          <button 
            onClick={retourRecherche}
            className="notefinale-button notefinale-button-primary"
          >
            <span>🌸</span> Retour à la recherche
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="notefinale-container">
      <h2 className="notefinale-title">
        <span>✨</span> Ta note finale <span>✨</span>
      </h2>

      {/* Prévisualisation JSON (cachée dans un details) */}
      <details className="notefinale-preview">
        <summary>Voir les données brutes</summary>
        <pre>
          {JSON.stringify(ankizy, null, 2)}
        </pre>
      </details>

      {/* Carte des résultats */}
      <div className="notefinale-card">
        <div className="notefinale-item">
          <div className="notefinale-icon">👩‍🎓</div>
          <span className="notefinale-label">Étudiante</span>
          <span className="notefinale-value">{ankizy.nom || "Non défini"}</span>
        </div>

        <div className="notefinale-item">
          <div className="notefinale-icon">📚</div>
          <span className="notefinale-label">Matière</span>
          <span className="notefinale-value">{ankizy.Matiere || ankizy.matiere || "Non défini"}</span>
        </div>

        <div className="notefinale-item">
          <div className="notefinale-icon">⭐</div>
          <span className="notefinale-label">Note</span>
          <span className="notefinale-value">{ankizy.Note || ankizy.note || "Non défini"} /20</span>
        </div>
      </div>

      {/* Boutons d'action */}
      <div className="notefinale-buttons">
        <button 
          onClick={retourRecherche}
          className="notefinale-button notefinale-button-secondary"
        >
          <span>🔍</span> Nouvelle recherche
        </button>
        <button 
          onClick={logout}
          className="notefinale-button notefinale-button-primary"
        >
          <span>💕</span> Déconnexion
        </button>
      </div>

      {/* Pétales décoratifs (optionnel) */}
      <div className="petale" style={{left: '10%', animationDelay: '0s'}}>🌸</div>
      <div className="petale" style={{left: '30%', animationDelay: '2s'}}>🌼</div>
      <div className="petale" style={{left: '50%', animationDelay: '1s'}}>🌸</div>
      <div className="petale" style={{left: '70%', animationDelay: '3s'}}>🌺</div>
      <div className="petale" style={{left: '90%', animationDelay: '2.5s'}}>🌸</div>
    </div>
  );
}


