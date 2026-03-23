import { BrowserRouter, Routes, Route } from "react-router-dom";
import AuthProvider from "./AuthContext";
import NoteProvider from "./Notecontext";
import Navigation from "./Navigation";  // ← IMPORT DE LA NAVIGATION
import MijeryNoteFinale from "./mijeryNoteFinale";
import FormulaireNote from "./FormulaireNote";
import NoteFinale from "./notefinale";
import ListeNotes from "./ListeNotes";
import ModifierNote from "./ModifierNote";
import FormulaireParametre from "./FormulaireParametre";
import ListeParametres from "./ListeParametres";
import ModifierParametre from "./ModifierParametre";

function App() {
  return (
    <AuthProvider>
      <NoteProvider>
        <BrowserRouter>
          <Navigation />  {/* ← LA NAVIGATION APPARAÎT SUR TOUTES LES PAGES */}
          <Routes>
            <Route path="/" element={<MijeryNoteFinale />} />
            <Route path="/formulaire-note" element={<FormulaireNote />} />
            <Route path="/notefinale" element={<NoteFinale />} />
            <Route path="/liste-notes" element={<ListeNotes />} />
            <Route path="/modifier-note/:id" element={<ModifierNote />} />

            
            <Route path="/formulaire-parametre" element={<FormulaireParametre />} />
            <Route path="/liste-parametres" element={<ListeParametres />} />
            <Route path="/modifier-parametre/:id" element={<ModifierParametre />} />
          </Routes>
        </BrowserRouter>
      </NoteProvider>
    </AuthProvider>
  );
}

export default App;