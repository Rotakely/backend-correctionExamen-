import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import "./ModifierParametre.css";  // ← IMPORT DU CSS

export default function ModifierParametre() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [data, setData] = useState({
    operateurId: "",
    matiereId: "",
    resolutionId: "",
    diff: ""
  });
  
  const [operateurs, setOperateurs] = useState([]);
  const [matieres, setMatieres] = useState([]);
  const [resolutions, setResolutions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);
  const [parametreOriginal, setParametreOriginal] = useState(null);

  useEffect(() => {
    chargerDonnees();
  }, []);

  async function chargerDonnees() {
    try {
      const [resOperateurs, resMatieres, resResolutions, resParametre] = await Promise.all([
        fetch("http://localhost:8080/api/operateur"),
        fetch("http://localhost:8080/api/matieres-pour-parametre"),
        fetch("http://localhost:8080/api/resolution"),
        fetch(`http://localhost:8080/api/parametres/${id}`)
      ]);

      const dataOperateurs = await resOperateurs.json();
      const dataMatieres = await resMatieres.json();
      const dataResolutions = await resResolutions.json();
      const parametre = await resParametre.json();

      console.log("Paramètre reçu:", parametre);

      setOperateurs(dataOperateurs);
      setMatieres(dataMatieres);
      setResolutions(dataResolutions);
      setParametreOriginal(parametre);
      
      setData({
        operateurId: String(parametre.operateur?.id || parametre.operateurId || ""),
        matiereId: String(parametre.matiere?.id || parametre.matiereId || ""),
        resolutionId: String(parametre.resolution?.id || parametre.resolutionId || ""),
        diff: parametre.diff || ""
      });

      setLoading(false);
    } catch (error) {
      setError("Erreur de chargement: " + error.message);
      setLoading(false);
    }
  }

  function handleChange(e) {
    setData({ ...data, [e.target.name]: e.target.value });
    setSuccess(false);
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
      const response = await fetch(`http://localhost:8080/api/parametres/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const errorText = await response.text();
        setError(`Erreur ${response.status}: ${errorText}`);
        setLoading(false);
        return;
      }

      const parametreModifie = await response.json();
      setSuccess(true);
      
      setTimeout(() => {
        navigate("/liste-parametres");
      }, 2000);

    } catch (error) {
      setError("Erreur de connexion: " + error.message);
    } finally {
      setLoading(false);
    }
  }

  if (loading) return <div className="modifparam-loading-message">Chargement du paramètre...</div>;
  if (error) return <div className="modifparam-error">{error}</div>;

  return (
    <div className="modifparam-container">
      <h2 className="modifparam-title">
        <span>⚙️</span> Modifier le paramètre <span className="modifparam-id">#{id}</span> <span>✨</span>
      </h2>
      
      {error && <div className="modifparam-error">{error}</div>}

      {success && (
        <div className="modifparam-success">
          <h3>✅ Paramètre modifié avec succès !</h3>
          <p>Redirection vers la liste...</p>
        </div>
      )}

      {/* Carte des valeurs actuelles */}
      {parametreOriginal && (
        <div className="modifparam-card">
          <h4>🌸 Valeurs actuelles</h4>
          <div className="modifparam-card-grid">
            <div className="modifparam-card-item">
              <div className="modifparam-card-label">⚙️ Opérateur</div>
              <div className="modifparam-card-value">
                {parametreOriginal.operateur?.operateur} (ID: {parametreOriginal.operateur?.id})
              </div>
            </div>
            <div className="modifparam-card-item">
              <div className="modifparam-card-label">📚 Matière</div>
              <div className="modifparam-card-value">
                {parametreOriginal.matiere?.nom} (ID: {parametreOriginal.matiere?.id})
              </div>
            </div>
            <div className="modifparam-card-item">
              <div className="modifparam-card-label">🎯 Résolution</div>
              <div className="modifparam-card-value">
                {parametreOriginal.resolution?.resolution} (ID: {parametreOriginal.resolution?.id})
              </div>
            </div>
            <div className="modifparam-card-item">
              <div className="modifparam-card-label">📏 Différence</div>
              <div className="modifparam-card-value">{parametreOriginal.diff}</div>
            </div>
          </div>
        </div>
      )}

      <form onSubmit={handleSubmit} className="modifparam-form">
        <div className="modifparam-group">
          <label className="modifparam-label">Nouvel opérateur :</label>
          <select 
            name="operateurId"
            value={data.operateurId}
            onChange={handleChange}
            required
            className="modifparam-select"
          >
            <option value="">Choisis un opérateur ⚙️</option>
            {operateurs.map(op => (
              <option key={op.id} value={String(op.id)}>
                {op.operateur} (ID: {op.id})
              </option>
            ))}
          </select>
        </div>
        
        <div className="modifparam-group">
          <label className="modifparam-label">Nouvelle matière :</label>
          <select 
            name="matiereId"
            value={data.matiereId}
            onChange={handleChange}
            required
            className="modifparam-select"
          >
            <option value="">Choisis une matière 📚</option>
            {matieres.map(matiere => (
              <option key={matiere.id} value={String(matiere.id)}>
                {matiere.nom} (Coeff: {matiere.coeff})
              </option>
            ))}
          </select>
        </div>
        
        <div className="modifparam-group">
          <label className="modifparam-label">Nouvelle résolution :</label>
          <select 
            name="resolutionId"
            value={data.resolutionId}
            onChange={handleChange}
            required
            className="modifparam-select"
          >
            <option value="">Choisis une résolution 🎯</option>
            {resolutions.map(res => (
              <option key={res.id} value={String(res.id)}>
                {res.resolution} (ID: {res.id})
              </option>
            ))}
          </select>
        </div>
        
        <div className="modifparam-group">
          <label className="modifparam-label">Nouvelle différence :</label>
          <input 
            name="diff"
            type="number" 
            step="0.01"
            placeholder="Valeur de différence" 
            value={data.diff}
            onChange={handleChange} 
            required
            className="modifparam-input"
          />
        </div>
        
        <div className="modifparam-buttons">
          <button 
            type="submit" 
            disabled={loading}
            className="modifparam-button modifparam-button-save"
          >
            {loading ? (
              <>
                <span className="modifparam-loading"></span>
                Modification...
              </>
            ) : "💾 Enregistrer"}
          </button>
          
          <button 
            type="button" 
            onClick={() => navigate("/liste-parametres")}
            className="modifparam-button modifparam-button-cancel"
          >
            Annuler
          </button>
        </div>
      </form>

      {/* Pétales décoratifs */}
      <div className="modifparam-petale" style={{left: '5%', animationDelay: '0s'}}>🌸</div>
      <div className="modifparam-petale" style={{left: '20%', animationDelay: '2s'}}>🌼</div>
      <div className="modifparam-petale" style={{left: '40%', animationDelay: '1s'}}>🌸</div>
      <div className="modifparam-petale" style={{left: '60%', animationDelay: '3s'}}>🌺</div>
      <div className="modifparam-petale" style={{left: '80%', animationDelay: '1.5s'}}>🌸</div>
      <div className="modifparam-petale" style={{left: '95%', animationDelay: '2.5s'}}>🌼</div>
    </div>
  );
}