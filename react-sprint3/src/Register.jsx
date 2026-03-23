import { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "./AuthContext";

export default function Register() {

  const [data, setData] = useState({
    nom: "",
    prenom: "",
    age: 0,
    cin: "",
    password: ""
  });

  const [error, setError] = useState("");
  const navigate = useNavigate();
  const { setUser } = useContext(AuthContext);

  function handleChange(e) {
    setData({ ...data, [e.target.name]: e.target.value });
  }

  async function register(e) {
    e.preventDefault();

    const response = await fetch("http://localhost:8080/api/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    });

    if (!response.ok) {
      setError("Erreur inscription");
      return;
    }

    const user = await response.json();
    setUser(user);
    navigate("/dashboard");
  }

  return (
    <>
      <h2>Register</h2>
      {error && <p style={{color:"red"}}>{error}</p>}

      <form onSubmit={register}>
        <input name="nom" placeholder="Nom" onChange={handleChange} />
        <br />
        <input name="prenom" placeholder="Prénom" onChange={handleChange} />
        <br />
        <input name="age" placeholder="Age" onChange={handleChange} />
        <br />
        <input name="cin" placeholder="CIN" onChange={handleChange} />
        <br />
        <input name="password" type="password" placeholder="Password" onChange={handleChange} />
        <br />
        <button type="submit">S'inscrire</button>
      </form>

      <button onClick={() => navigate("/")}>Login</button>
    </>
  );
}