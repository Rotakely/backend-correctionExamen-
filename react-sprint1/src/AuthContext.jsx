import { createContext, useState, useEffect } from "react";

export const AuthContext = createContext();

export default function AuthProvider({ children }) {


  const [ankizy, setAnkizy] = useState(() => {
    const saved = localStorage.getItem("ankizy");  // Changez aussi la clé localStorage
    return saved ? JSON.parse(saved) : null;
  });


  useEffect(() => {
    if (ankizy) {
      localStorage.setItem("ankizy", JSON.stringify(ankizy));  // Changez la clé
    } else {
      localStorage.removeItem("ankizy");  // Changez la clé
    }
  }, [ankizy]);

  return (

    <AuthContext.Provider value={{ ankizy, setAnkizy }}>
      {children}
    </AuthContext.Provider>
  );
}

