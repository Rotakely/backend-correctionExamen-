import { createContext, useState, useEffect } from "react";

export const Notecontext = createContext();

export default function NoteProvider({ children }) {  
  const [note, setNote] = useState(() => {
    const saved = localStorage.getItem("note");
    return saved ? JSON.parse(saved) : null;
  });

  useEffect(() => {
    if (note) {
      localStorage.setItem("note", JSON.stringify(note));
    } else {
      localStorage.removeItem("note");
    }
  }, [note]);

  return (
    <Notecontext.Provider value={{ note, setNote }}>  
      {children}  
    </Notecontext.Provider>
  );
}



// import { Notecontext } from "./Notecontext";

// export default function FormulaireNote() {
//   const { setNote } = useContext(Notecontext);  // Je demande le "bouton setNote"
  
//   async function handleSubmit() {
//     // ... après avoir ajouté la note ...
//     const nouvelleNote = await response.json();
//     setNote(nouvelleNote);  // Je range la note dans le tiroir
//     // La note est maintenant disponible partout !
//   }
// }


// 3. IL PERMET À TOUS LES COMPOSANTS D'ACCÉDER À LA NOTE


// Sans useContext (pas de frigo commun) :

// Composant A (FormulaireNote)      Composant B (NoteFinale)
//     ┌──────────┐                      ┌──────────┐
//     │ note = 15│                      │  ????    │
//     └──────────┘                      └──────────┘
//           │                                 │
//           └──────────┬──────────────────────┘
//                      │
//               ❌ Impossible de partager !




// Avec useContext (avec frigo commun) :
// ┌─────────────────┐
// │   Notecontext   │
// │   (Le frigo)    │
// │   note = 15.5   │
// └─────────────────┘
//       ↗      ↖
// setNote      note
//    /            \
//   /              \
// Composant A      Composant B      Composant C
// (FormulaireNote) (NoteFinale)     (Autre)
// met 15.5         lit 15.5       lit 15.5

