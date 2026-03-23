import { BrowserRouter, Routes, Route } from "react-router-dom";
import ClientForm from "./components/ClientForm.jsx"; 
import DemandeForm from "./components/DemandeForm.jsx"; 

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Cette route affiche ton CRUD Client sur la page d'accueil */}
        <Route path="/" element={<ClientForm />} />
        
        {/* Tu peux aussi y accéder via /clients */}
        <Route path="/clients" element={<ClientForm />} />
        <Route path="/demande" element={<DemandeForm />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;