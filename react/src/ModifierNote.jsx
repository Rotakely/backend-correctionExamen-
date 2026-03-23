import { useState, useEffect, useContext } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Notecontext } from "./Notecontext";
import "./ModifierNote.css";  // ← IMPORT DU CSS

export default function ModifierNote() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { setNote } = useContext(Notecontext);

  const [data, setData] = useState({
    etudiantId: "",
    matiereId: "",
    correcteurId: "",
    note: ""
  });
  const [etudiants, setEtudiants] = useState([]);
  const [matieres, setMatieres] = useState([]);
  const [correcteurs, setCorrecteurs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);  // ← AJOUTÉ

  useEffect(() => {
    chargerDonnees();
  }, []);

  async function chargerDonnees() {
    try {
      const [resEtudiants, resMatieres, resCorrecteurs, resNote] = await Promise.all([
        fetch("http://localhost:8080/api/etudiants"),
        fetch("http://localhost:8080/api/matieres"),
        fetch("http://localhost:8080/api/correcteurs"),
        fetch(`http://localhost:8080/api/notes/${id}`)
      ]);
  
      const dataEtudiants = await resEtudiants.json();
      const dataMatieres = await resMatieres.json();
      const dataCorrecteurs = await resCorrecteurs.json();
      const note = await resNote.json();
  
      setEtudiants(dataEtudiants);
      setMatieres(dataMatieres);
      setCorrecteurs(dataCorrecteurs);
      
      setData({
        etudiantId: String(note.etudiant?.id || note.etudiantId || ""),
        matiereId: String(note.matiere?.id || note.matiereId || ""),
        correcteurId: String(note.correcteur?.id || note.correcteurId || ""),
        note: note.note || ""
      });
  
      setLoading(false);
    } catch (error) {
      setError("Erreur de chargement");
      setLoading(false);
    }
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);
    setError("");
    setSuccess(false);

    const payload = {
      etudiantId: parseInt(data.etudiantId),
      matiereId: parseInt(data.matiereId),
      correcteurId: parseInt(data.correcteurId),
      note: parseFloat(data.note)
    };

    try {
      const response = await fetch(`http://localhost:8080/api/notes/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });

      if (!response.ok) throw new Error("Erreur modification");

      const noteModifiee = await response.json();
      setNote(noteModifiee);
      setSuccess(true);
      
      // Redirection après 1.5 seconde
      setTimeout(() => {
        navigate("/liste-notes");
      }, 1500);

    } catch (error) {
      setError("Erreur: " + error.message);
    } finally {
      setLoading(false);
    }
  }

  if (loading) return <div className="modifier-loading-message">Chargement de la note...</div>;
  if (error) return <div className="modifier-error">{error}</div>;

  return (
    <div className="modifier-container">
      <h2 className="modifier-title">
        <span>✨</span> Modifier la note <span className="modifier-id">#{id}</span> <span>✨</span>
      </h2>
      
      {success && (
        <div className="modifier-success">
          <h3>✅ Note modifiée avec succès !</h3>
          <p>Redirection vers la liste...</p>
        </div>
      )}

      {/* Carte des valeurs actuelles */}
      <div className="modifier-debug">
        <h4>🌸 Valeurs actuelles</h4>
        <div className="modifier-debug-grid">
          <div className="modifier-debug-item">
            <div className="modifier-debug-label">Étudiante</div>
            <div className="modifier-debug-value">
              {etudiants.find(e => e.id === parseInt(data.etudiantId))?.nom || data.etudiantId}
            </div>
          </div>
          <div className="modifier-debug-item">
            <div className="modifier-debug-label">Matière</div>
            <div className="modifier-debug-value">
              {matieres.find(m => m.id === parseInt(data.matiereId))?.nom || data.matiereId}
            </div>
          </div>
          <div className="modifier-debug-item">
            <div className="modifier-debug-label">Correctrice</div>
            <div className="modifier-debug-value">
              {correcteurs.find(c => c.id === parseInt(data.correcteurId))?.nom || data.correcteurId}
            </div>
          </div>
          <div className="modifier-debug-item">
            <div className="modifier-debug-label">Note</div>
            <div className="modifier-debug-value">{data.note}/20</div>
          </div>
        </div>
      </div>

      <form onSubmit={handleSubmit} className="modifier-form">
        <div className="modifier-group">
          <label className="modifier-label">Nouvelle étudiante :</label>
          <select 
            name="etudiantId" 
            value={data.etudiantId} 
            onChange={(e) => setData({...data, etudiantId: e.target.value})} 
            required
            className="modifier-select"
          >
            <option value="">Choisis une étudiante ✨</option>
            {etudiants.map(e => (
              <option key={e.id} value={String(e.id)}>{e.nom}</option>
            ))}
          </select>
        </div>
        
        <div className="modifier-group">
          <label className="modifier-label">Nouvelle matière :</label>
          <select 
            name="matiereId" 
            value={data.matiereId} 
            onChange={(e) => setData({...data, matiereId: e.target.value})} 
            required
            className="modifier-select"
          >
            <option value="">Choisis une matière 📚</option>
            {matieres.map(m => (
              <option key={m.id} value={String(m.id)}>{m.nom}</option>
            ))}
          </select>
        </div>
        
        <div className="modifier-group">
          <label className="modifier-label">Nouvelle correctrice :</label>
          <select 
            name="correcteurId" 
            value={data.correcteurId} 
            onChange={(e) => setData({...data, correcteurId: e.target.value})} 
            required
            className="modifier-select"
          >
            <option value="">Choisis une correctrice 👩‍🏫</option>
            {correcteurs.map(c => (
              <option key={c.id} value={String(c.id)}>{c.nom}</option>
            ))}
          </select>
        </div>
        
        <div className="modifier-group">
          <label className="modifier-label">Nouvelle note :</label>
          <input 
            type="number" 
            step="0.01" 
            value={data.note} 
            onChange={(e) => setData({...data, note: e.target.value})} 
            required 
            className="modifier-input"
          />
        </div>
        
        <div className="modifier-buttons">
          <button 
            type="submit" 
            disabled={loading}
            className="modifier-button modifier-button-save"
          >
            {loading ? (
              <>
                <span className="modifier-loading"></span>
                Modification...
              </>
            ) : "💕 Enregistrer"}
          </button>
          <button 
            type="button" 
            onClick={() => navigate("/liste-notes")}
            className="modifier-button modifier-button-cancel"
          >
            Annuler
          </button>
        </div>
      </form>

      {/* Pétales décoratifs */}
      <div className="modifier-petale" style={{left: '5%', animationDelay: '0s'}}>🌸</div>
      <div className="modifier-petale" style={{left: '20%', animationDelay: '2s'}}>🌼</div>
      <div className="modifier-petale" style={{left: '40%', animationDelay: '1s'}}>🌸</div>
      <div className="modifier-petale" style={{left: '60%', animationDelay: '3s'}}>🌺</div>
      <div className="modifier-petale" style={{left: '80%', animationDelay: '1.5s'}}>🌸</div>
      <div className="modifier-petale" style={{left: '95%', animationDelay: '2.5s'}}>🌼</div>
    </div>
  );
}