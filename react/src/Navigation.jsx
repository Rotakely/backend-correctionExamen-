import { Link, useLocation } from "react-router-dom";
import "./Navigation.css"; // On va créer ce fichier CSS

export default function Navigation() {
  const location = useLocation();
  
  // Fonction pour savoir si un lien est actif
  const isActive = (path) => {
    return location.pathname === path;
  };

  return (
    <nav className="navigation">
      <div className="nav-container">
        <div className="nav-logo">
          <Link to="/" className="nav-logo-link">
            <span className="nav-logo-icon">📚</span>
            <span className="nav-logo-text">Gestion Notes</span>
          </Link>
        </div>

        <div className="nav-menu">
          {/* Section Notes */}
          <div className="nav-section">
            <div className="nav-section-title">Notes</div>
            <Link 
              to="/" 
              className={`nav-link ${isActive('/') ? 'active' : ''}`}
            >
              <span className="nav-icon">🔍</span>
              <span className="nav-label">Consulter</span>
            </Link>
            <Link 
              to="/formulaire-note" 
              className={`nav-link ${isActive('/formulaire-note') ? 'active' : ''}`}
            >
              <span className="nav-icon">➕</span>
              <span className="nav-label">Ajouter</span>
            </Link>
            <Link 
              to="/liste-notes" 
              className={`nav-link ${isActive('/liste-notes') ? 'active' : ''}`}
            >
              <span className="nav-icon">📋</span>
              <span className="nav-label">Liste</span>
            </Link>
          </div>

          {/* Section Paramètres */}
          <div className="nav-section">
            <div className="nav-section-title">Paramètres</div>
            <Link 
              to="/formulaire-parametre" 
              className={`nav-link ${isActive('/formulaire-parametre') ? 'active' : ''}`}
            >
              <span className="nav-icon">⚙️</span>
              <span className="nav-label">Ajouter</span>
            </Link>
            <Link 
              to="/liste-parametres" 
              className={`nav-link ${isActive('/liste-parametres') ? 'active' : ''}`}
            >
              <span className="nav-icon">📋</span>
              <span className="nav-label">Liste</span>
            </Link>
          </div>
        </div>
      </div>
    </nav>
  );
}