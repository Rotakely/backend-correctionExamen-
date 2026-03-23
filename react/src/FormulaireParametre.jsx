import { useState, useEffect } from "react";
import "./FormulaireParametre.css";  // ← IMPORT DU CSS

export default function FormulaireParametre() {
  const [data, setData] = useState({ 
    operateurId: "", 
    matiereId: "", 
    resolutionId: "", 
    diff: "" 
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [diffAjoutee, setDiffAjoutee] = useState(null);
  
  // États pour les listes déroulantes
  const [operateurs, setOperateurs] = useState([]);
  const [matieres, setMatieres] = useState([]);
  const [resolutions, setResolutions] = useState([]);
  const [loadingLists, setLoadingLists] = useState(true);

  useEffect(() => {
    chargerDonnees();
  }, []);

  async function chargerDonnees() {
    try {
      const resOperateurs = await fetch("http://localhost:8080/api/operateur");
      const dataOperateurs = await resOperateurs.json();
      setOperateurs(dataOperateurs);
  
      const resMatieres = await fetch("http://localhost:8080/api/matieres-pour-parametre");
      const dataMatieres = await resMatieres.json();
      setMatieres(dataMatieres);
  
      const resResolutions = await fetch("http://localhost:8080/api/resolution");
      const dataResolutions = await resResolutions.json();
      setResolutions(dataResolutions);
  
      setLoadingLists(false);
    } catch (error) {
      setError("Impossible de charger les données");
      setLoadingLists(false);
    }
  }

  function handleChange(e) {
    setData({ ...data, [e.target.name]: e.target.value });
    setSuccess(false);
    setDiffAjoutee(null);
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);
    setError("");
    setSuccess(false);

    if (!data.operateurId || !data.matiereId || !data.resolutionId || !data.diff) {
      setError("Veuillez remplir tous les champs");
      setLoading(false);
      return;
    }

    const payload = {
      operateurId: parseInt(data.operateurId),
      matiereId: parseInt(data.matiereId),
      resolutionId: parseInt(data.resolutionId),
      diff: parseFloat(data.diff)
    };

    try {
      const response = await fetch("http://localhost:8080/api/parametres", {
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

      const nouvelleDiff = await response.json();
      setSuccess(true);
      setDiffAjoutee(nouvelleDiff);
      
      setData({ 
        operateurId: "", 
        matiereId: "", 
        resolutionId: "", 
        diff: "" 
      });

    } catch (error) {
      setError("Erreur de connexion: " + error.message);
    } finally {
      setLoading(false);
    }
  }

  if (loadingLists) {
    return (
      <div className="param-loading-lists">
        Chargement des données...
      </div>
    );
  }

  return (
    <div className="param-container">
      <h2 className="param-title">
        <span>⚙️</span> Ajouter un paramètre <span>✨</span>
      </h2>
      
      {error && <div className="param-error">{error}</div>}

      {success && diffAjoutee && (
        <div className="param-success">
          <h3>✅ Paramètre enregistré !</h3>
          <p><strong>Opérateur:</strong> {diffAjoutee.operateur?.operateur || diffAjoutee.operateurId}</p>
          <p><strong>Matière:</strong> {diffAjoutee.matiere?.nom || diffAjoutee.matiereId}</p>
          <p><strong>Résolution:</strong> {diffAjoutee.resolution?.resolution || diffAjoutee.resolutionId}</p>
          <p><strong>Différence:</strong> {diffAjoutee.diff}</p>
        </div>
      )}

      <form onSubmit={handleSubmit} className="param-form">
        <div className="param-group">
          <label className="param-label">Opérateur :</label>
          <select 
            name="operateurId"
            value={data.operateurId}
            onChange={handleChange}
            required
            className="param-select"
          >
            <option value="">Choisis un opérateur ⚙️</option>
            {operateurs.map(op => (
              <option key={op.id} value={op.id}>
                {op.operateur} (ID: {op.id})
              </option>
            ))}
          </select>
        </div>
        
        <div className="param-group">
          <label className="param-label">Matière :</label>
          <select 
            name="matiereId"
            value={data.matiereId}
            onChange={handleChange}
            required
            className="param-select"
          >
            <option value="">Choisis une matière 📚</option>
            {matieres.map(matiere => (
              <option key={matiere.id} value={matiere.id}>
                {matiere.nom} (Coeff: {matiere.coeff})
              </option>
            ))}
          </select>
        </div>
        
        <div className="param-group">
          <label className="param-label">Résolution :</label>
          <select 
            name="resolutionId"
            value={data.resolutionId}
            onChange={handleChange}
            required
            className="param-select"
          >
            <option value="">Choisis une résolution 🎯</option>
            {resolutions.map(res => (
              <option key={res.id} value={res.id}>
                {res.resolution} (ID: {res.id})
              </option>
            ))}
          </select>
        </div>
        
        <div className="param-group">
          <label className="param-label">Différence :</label>
          <input 
            name="diff"
            type="number" 
            step="0.01"
            placeholder="Valeur de différence" 
            value={data.diff}
            onChange={handleChange} 
            required
            className="param-input"
          />
          <div className="param-helper">Valeur seuil pour la règle</div>
        </div>
        
        <button 
          type="submit" 
          disabled={loading}
          className="param-button"
        >
          {loading ? (
            <>
              <span className="param-loading"></span>
              Enregistrement...
            </>
          ) : "⚙️ Enregistrer le paramètre"}
        </button>
      </form>

      {/* Pétales décoratifs */}
      <div className="param-petale" style={{left: '5%', animationDelay: '0s'}}>🌸</div>
      <div className="param-petale" style={{left: '20%', animationDelay: '2s'}}>🌼</div>
      <div className="param-petale" style={{left: '40%', animationDelay: '1s'}}>🌸</div>
      <div className="param-petale" style={{left: '60%', animationDelay: '3s'}}>🌺</div>
      <div className="param-petale" style={{left: '80%', animationDelay: '1.5s'}}>🌸</div>
      <div className="param-petale" style={{left: '95%', animationDelay: '2.5s'}}>🌼</div>
    </div>
  );
}