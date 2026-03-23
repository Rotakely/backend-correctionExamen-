import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import "./ListeNotes.css";  // ← IMPORT DU CSS

export default function ListeNotes() {
  const [notes, setNotes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    chargerNotes();
  }, []);

  async function chargerNotes() {
    try {
      const response = await fetch("http://localhost:8080/api/notes");
      const data = await response.json();
      setNotes(data);
      setLoading(false);
    } catch (error) {
      setError("Erreur de chargement");
      setLoading(false);
    }
  }

  async function supprimerNote(id) {
    if (window.confirm("Supprimer cette note ?")) {
      try {
        await fetch(`http://localhost:8080/api/notes/${id}`, {
          method: "DELETE"
        });
        chargerNotes();
      } catch (error) {
        setError("Erreur lors de la suppression");
      }
    }
  }

  if (loading) return <div className="liste-loading">Chargement des notes...</div>;
  if (error) return <div className="liste-error">{error}</div>;

  return (
    <div className="liste-container">
      <h2 className="liste-title">
        <span>✨</span> Liste des notes <span>✨</span>
      </h2>
      
      <Link to="/formulaire-note" className="liste-add-button">
        <button>🌸 Ajouter une note</button>
      </Link>
      
      {notes.length === 0 ? (
        <div className="liste-empty">
          Aucune note pour le moment
        </div>
      ) : (
        <div className="liste-table-container">
          <table className="liste-table">
            <thead className="liste-thead">
              <tr>
                <th>ID</th>
                <th>Étudiante</th>
                <th>Matière</th>
                <th>Correctrice</th>
                <th>Note</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody className="liste-tbody">
              {notes.map(note => (
                <tr key={note.id}>
                  <td data-label="ID">{note.id}</td>
                  <td data-label="Étudiante">{note.etudiant?.nom || note.etudiantId}</td>
                  <td data-label="Matière">{note.matiere?.nom || note.matiereId}</td>
                  <td data-label="Correctrice">{note.correcteur?.nom || note.correcteurId}</td>
                  <td data-label="Note">
                    <span className="liste-note-badge">{note.note}/20</span>
                  </td>
                  <td data-label="Actions">
                    <Link to={`/modifier-note/${note.id}`} className="liste-action-button modifier">
                      ✏️ Modifier
                    </Link>
                    <button 
                      onClick={() => supprimerNote(note.id)} 
                      className="liste-action-button supprimer"
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
      <div className="liste-petale" style={{left: '5%', animationDelay: '0s'}}>🌸</div>
      <div className="liste-petale" style={{left: '20%', animationDelay: '2s'}}>🌼</div>
      <div className="liste-petale" style={{left: '40%', animationDelay: '1s'}}>🌸</div>
      <div className="liste-petale" style={{left: '60%', animationDelay: '3s'}}>🌺</div>
      <div className="liste-petale" style={{left: '80%', animationDelay: '1.5s'}}>🌸</div>
      <div className="liste-petale" style={{left: '95%', animationDelay: '2.5s'}}>🌼</div>
    </div>
  );
}