import "./FormulaireNote.css";  // ← IMPORT DU CSS
import { useState, useContext, useEffect } from "react";
import { Notecontext } from "./Notecontext";

export default function FormulaireNote() {
  const [data, setData] = useState({ 
    etudiantId: "", 
    matiereId: "", 
    correcteurId: "", 
    note: "" 
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [noteAjoutee, setNoteAjoutee] = useState(null);
  
  // États pour les listes déroulantes
  const [etudiants, setEtudiants] = useState([]);
  const [matieres, setMatieres] = useState([]);
  const [correcteurs, setCorrecteurs] = useState([]);
  const [loadingLists, setLoadingLists] = useState(true);

  const { setNote } = useContext(Notecontext);

  useEffect(() => {
    chargerDonnees();
  }, []);

  async function chargerDonnees() {
    try {
      const resEtudiants = await fetch("http://localhost:8080/api/etudiants");
      const dataEtudiants = await resEtudiants.json();
      setEtudiants(dataEtudiants);
  
      const resMatieres = await fetch("http://localhost:8080/api/matieres");
      const dataMatieres = await resMatieres.json();
      setMatieres(dataMatieres);
  
      const resCorrecteurs = await fetch("http://localhost:8080/api/correcteurs");
      const dataCorrecteurs = await resCorrecteurs.json();
      setCorrecteurs(dataCorrecteurs);
  
      setLoadingLists(false);
    } catch (error) {
      setError("Impossible de charger les données");
      setLoadingLists(false);
    }
  }

  function handleChange(e) {
    setData({ ...data, [e.target.name]: e.target.value });
    setSuccess(false);
    setNoteAjoutee(null);
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);
    setError("");
    setSuccess(false);

    if (!data.etudiantId || !data.matiereId || !data.correcteurId || !data.note) {
      setError("Veuillez remplir tous les champs");
      setLoading(false);
      return;
    }

    const payload = {
      etudiantId: parseInt(data.etudiantId),
      matiereId: parseInt(data.matiereId),
      correcteurId: parseInt(data.correcteurId),
      note: parseFloat(data.note)
    };

    try {
      const response = await fetch("http://localhost:8080/api/notes", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const errorText = await response.text();
        setError(`Erreur ${response.status}: ${errorText}`);
        setLoading(false);
        return;
      }

      const nouvelleNote = await response.json();
      setNote(nouvelleNote);

      setSuccess(true);
      setNoteAjoutee(nouvelleNote);
      
      setData({ 
        etudiantId: "", 
        matiereId: "", 
        correcteurId: "", 
        note: "" 
      });

    } catch (error) {
      setError("Erreur de connexion: " + error.message);
    } finally {
      setLoading(false);
    }
  }

  if (loadingLists) {
    return (
      <div className="formnote-loading-lists">
        Chargement des données...
      </div>
    );
  }

  return (
    <div className="formnote-container">
      <h2 className="formnote-title">
        <span>✨</span> Ajouter une note <span>✨</span>
      </h2>
      
      {error && <div className="formnote-error">{error}</div>}

      {success && noteAjoutee && (
        <div className="formnote-success">
          <h3> Note enregistrée !</h3>
          <p><strong>Étudiant:</strong> {noteAjoutee.etudiant?.nom || noteAjoutee.etudiantId}</p>
          <p><strong>Matière:</strong> {noteAjoutee.matiere?.nom || noteAjoutee.matiereId}</p>
          <p><strong>Correcteur:</strong> {noteAjoutee.correcteur?.nom || noteAjoutee.correcteurId}</p>
          <p><strong>Note:</strong> {noteAjoutee.note}/20</p>
        </div>
      )}

      <form onSubmit={handleSubmit} className="formnote-form">
        <div className="formnote-group">
          <label className="formnote-label">Étudiante :</label>
          <select 
            name="etudiantId"
            value={data.etudiantId}
            onChange={handleChange}
            required
            className="formnote-select"
          >
            <option value="">Choisis une étudiante ✨</option>
            {etudiants.map(etudiant => (
              <option key={etudiant.id} value={etudiant.id}>
                {etudiant.nom} (ID: {etudiant.id})
              </option>
            ))}
          </select>
        </div>
        
        <div className="formnote-group">
          <label className="formnote-label">Matière :</label>
          <select 
            name="matiereId"
            value={data.matiereId}
            onChange={handleChange}
            required
            className="formnote-select"
          >
            <option value="">Choisis une matière 📚</option>
            {matieres.map(matiere => (
              <option key={matiere.id} value={matiere.id}>
                {matiere.nom} (Coeff: {matiere.coeff})
              </option>
            ))}
          </select>
        </div>
        
        <div className="formnote-group">
          <label className="formnote-label">Correctrice :</label>
          <select 
            name="correcteurId"
            value={data.correcteurId}
            onChange={handleChange}
            required
            className="formnote-select"
          >
            <option value="">Choisis une correctrice 👩‍🏫</option>
            {correcteurs.map(correcteur => (
              <option key={correcteur.id} value={correcteur.id}>
                {correcteur.nom} (ID: {correcteur.id})
              </option>
            ))}
          </select>
        </div>
        
        <div className="formnote-group">
          <label className="formnote-label">Note :</label>
          <input 
            name="note"
            type="number" 
            step="0.01"
            min="0"
            max="20"
            placeholder="Note (0-20)" 
            value={data.note}
            onChange={handleChange} 
            required
            className="formnote-input"
          />
          <div className="formnote-helper">Entre 0 et 20 points</div>
        </div>
        
        <button 
          type="submit" 
          disabled={loading}
          className="formnote-button"
        >
          {loading ? (
            <>
              <span className="formnote-loading"></span>
              Enregistrement...
            </>
          ) : "🌸 Enregistrer la note"}
        </button>
      </form>

      {/* Pétales décoratifs */}
      <div className="formnote-petale" style={{left: '5%', animationDelay: '0s'}}>🌸</div>
      <div className="formnote-petale" style={{left: '20%', animationDelay: '2s'}}>🌼</div>
      <div className="formnote-petale" style={{left: '40%', animationDelay: '1s'}}>🌸</div>
      <div className="formnote-petale" style={{left: '60%', animationDelay: '3s'}}>🌺</div>
      <div className="formnote-petale" style={{left: '80%', animationDelay: '1.5s'}}>🌸</div>
      <div className="formnote-petale" style={{left: '95%', animationDelay: '2.5s'}}>🌼</div>
    </div>
  );
}