## Ce que le Contexte fait vraiment

| Sans Contexte | Avec Contexte |
|---|---|
| Les données voyagent de parent en enfant | Les données sont dans un "réservoir" central |
| Tous les composants intermédiaires doivent recevoir et retransmettre | Seuls les composants qui en ont besoin vont chercher la donnée |
| Si vous oubliez de passer une prop à un niveau, tout plante | Pas de risque d'oublier de passer la prop |
| Le code est plus long et moins lisible | Le code est plus propre et plus court |



Le contexte ne sert PAS à écrire { nom: "Jean"... } (ça c'est juste du JSON).

Le contexte sert à transporter cette donnée entre les composants sans avoir à la passer manuellement par les props.

C'est comme un service de messagerie instantanée pour vos données : n'importe quel composant peut envoyer ou recevoir sans connaître le chemin !

