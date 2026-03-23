import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import "./ListeParametres.css";  // ← IMPORT DU CSS

export default function ListeParametres() {
  const [parametres, setParametres] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    chargerParametres();
  }, []);

  async function chargerParametres() {
    try {
      const response = await fetch("http://localhost:8080/api/parametres");
      const data = await response.json();
      setParametres(data);
      setLoading(false);
    } catch (error) {
      setError("Erreur de chargement");
      setLoading(false);
    }
  }

  async function supprimerParametre(id) {
    if (window.confirm("Supprimer ce paramètre ?")) {
      try {
        await fetch(`http://localhost:8080/api/parametres/${id}`, {
          method: "DELETE"
        });
        chargerParametres();
      } catch (error) {
        setError("Erreur lors de la suppression");
      }
    }
  }

  if (loading) return <div className="listeparam-loading">Chargement des paramètres...</div>;
  if (error) return <div className="listeparam-error">{error}</div>;

  return (
    <div className="listeparam-container">
      <h2 className="listeparam-title">
        <span>⚙️</span> Liste des paramètres <span>✨</span>
      </h2>
      
      <Link to="/formulaire-parametre" className="listeparam-add-button">
        <button>⚙️ Ajouter un paramètre</button>
      </Link>
      
      {parametres.length === 0 ? (
        <div className="listeparam-empty">
          Aucun paramètre pour le moment
        </div>
      ) : (
        <div className="listeparam-table-container">
          <table className="listeparam-table">
            <thead className="listeparam-thead">
              <tr>
                <th>ID</th>
                <th>Opérateur</th>
                <th>Matière</th>
                <th>Résolution</th>
                <th>Différence</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody className="listeparam-tbody">
              {parametres.map(p => (
                <tr key={p.id}>
                  <td data-label="ID">{p.id}</td>
                  <td data-label="Opérateur">{p.operateur?.operateur || p.operateurId}</td>
                  <td data-label="Matière">{p.matiere?.nom || p.matiereId}</td>
                  <td data-label="Résolution">{p.resolution?.resolution || p.resolutionId}</td>
                  <td data-label="Différence">
                    <span className="listeparam-diff-badge">{p.diff}</span>
                  </td>
                  <td data-label="Actions">
                    <Link to={`/modifier-parametre/${p.id}`} className="listeparam-action-button modifier">
                      ✏️ Modifier
                    </Link>
                    <button 
                      onClick={() => supprimerParametre(p.id)} 
                      className="listeparam-action-button supprimer"
                    >
                      🗑️ Supprimer
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Pétales décoratifs */}
      <div className="listeparam-petale" style={{left: '5%', animationDelay: '0s'}}>🌸</div>
      <div className="listeparam-petale" style={{left: '20%', animationDelay: '2s'}}>🌼</div>
      <div className="listeparam-petale" style={{left: '40%', animationDelay: '1s'}}>🌸</div>
      <div className="listeparam-petale" style={{left: '60%', animationDelay: '3s'}}>🌺</div>
      <div className="listeparam-petale" style={{left: '80%', animationDelay: '1.5s'}}>🌸</div>
      <div className="listeparam-petale" style={{left: '95%', animationDelay: '2.5s'}}>🌼</div>
    </div>
  );
}