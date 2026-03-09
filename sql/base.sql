CREATE DATABASE correcteur_etudiant;
\c correcteur_etudiant;

CREATE TABLE Matiere (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    coeff INT
);

CREATE TABLE Etudiant (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL
);

CREATE TABLE Correcteur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL
);


CREATE TYPE type_operateur AS ENUM ('<', '>', '=');

CREATE TABLE Operateur (
    id SERIAL PRIMARY KEY,
    operateur type_operateur NOT NULL
);


CREATE TABLE Resolution (
    id SERIAL PRIMARY KEY,
    resolution VARCHAR(50) NOT NULL
  
);

CREATE TABLE Parametre (
    id SERIAL PRIMARY KEY,
    id_matiere INT REFERENCES Matiere(id) NOT NULL,
    diff DECIMAL(10,2) NOT NULL,
    id_operateur INT REFERENCES Operateur(id) NOT NULL,
    id_resolution INT REFERENCES Resolution(id) NOT NULL
);

CREATE TABLE Note (
    id SERIAL PRIMARY KEY,
    id_matiere INT REFERENCES Matiere(id) NOT NULL,
    note DECIMAL(10,2) NOT NULL,
    id_correcteur INT REFERENCES Correcteur(id) NOT NULL,
    id_etudiant INT REFERENCES Etudiant(id) NOT NULL
);


create table Notefinale(
     id SERIAL PRIMARY KEY,
     id_etudiant INT REFERENCES Etudiant(id) NOT NULL,
     id_note INT REFERENCES Note(id) NOT NULL
    
);


-- ===========================================
-- RÉINITIALISATION COMPLÈTE (les IDs repartent à 1)
-- ===========================================

-- Désactiver temporairement les contraintes de clés étrangères
ET session_replication_role = 'replica';

-- TRUNCATE toutes les tables dans l'ordre (dépendances)
TRUNCATE TABLE Notefinale RESTART IDENTITY CASCADE;
TRUNCATE TABLE Parametre RESTART IDENTITY CASCADE;
TRUNCATE TABLE Note RESTART IDENTITY CASCADE;
TRUNCATE TABLE Resolution RESTART IDENTITY CASCADE;
TRUNCATE TABLE Operateur RESTART IDENTITY CASCADE;
TRUNCATE TABLE Correcteur RESTART IDENTITY CASCADE;
TRUNCATE TABLE Etudiant RESTART IDENTITY CASCADE;
TRUNCATE TABLE Matiere RESTART IDENTITY CASCADE;

-- Réactiver les contraintes
SET session_replication_role = 'origin';
S


-- MATIERES
INSERT INTO Matiere (nom, coeff) VALUES ('Mathématiques', 3);  -- id=1      

-- ETUDIANTS
INSERT INTO Etudiant (nom) VALUES ('Jean');     -- id=1


-- CORRECTEURS
INSERT INTO Correcteur (nom) VALUES ('M. Dupont');   -- id=1
INSERT INTO Correcteur (nom) VALUES ('Mme Martin');  -- id=2
INSERT INTO Correcteur (nom) VALUES ('M.Rojo');  -- id=2

-- OPERATEURS (avec ENUM)
INSERT INTO Operateur (operateur) VALUES ('<');  -- id=1
INSERT INTO Operateur (operateur) VALUES ('>');  -- id=2
INSERT INTO Operateur (operateur) VALUES ('=');  -- id=3

-- RESOLUTIONS
INSERT INTO Resolution (resolution) VALUES ('MAX');      -- id=1
INSERT INTO Resolution (resolution) VALUES ('MIN');      -- id=2
INSERT INTO Resolution (resolution) VALUES ('MOYENNE');  -- id=3
INSERT INTO Resolution (resolution) VALUES ('SOMME');    -- id=4


-- EXEMPLE 1: Jean en Mathématiques - Notes identiques

-- Notes de Jean (id_etudiant=1) en Mathématiques (id_matiere=1)
INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) 
VALUES (1, 12.5, 1, 1);  -- Note 1
INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) 
VALUES (1, 12.5, 2, 1);  -- Note 2 (identique)
INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) 
VALUES (1, 12.5, 3, 1);  -- Note 3 (identique)



--  EXEMPLE 2: Jean en Mathématiques - Petites différences
-- Notes de Jean (id_etudiant=1) en Mathématiques (id_matiere=1)
INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) 
VALUES (1, 10.0, 1, 1);  -- Note 1
INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) 
VALUES (1, 11.0, 2, 1);  -- Note 2 (+1.0)
INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) 
VALUES (1, 11.5, 1, 1);  -- Note 3 (+0.5)


-- EXEMPLE 3: Jean en Mathématiques - Grandes différences
-- Notes de Jean (id_etudiant=1) en Mathématiques (id_matiere=1)
INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) 
VALUES (1, 5.0, 1, 1);   -- Note 1
INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) 
VALUES (1, 12.0, 2, 1);  -- Note 2 (+7.0)
INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) 
VALUES (1, 8.0, 1, 1);   -- Note 3 (-4.0)



-- EXEMPLE 4: Jean en Mathématiques - Différence exacte
-- Notes de Jean (id_etudiant=1) en Mathématiques (id_matiere=1)
INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) 
VALUES (1, 10.0, 1, 1);  -- Note 1
INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) 
VALUES (1, 13.0, 2, 1);  -- Note 2 (+3.0)




-- Exemple de paramètres pour une matière
INSERT INTO Parametre (id_matiere, diff, id_operateur, id_resolution) 
VALUES (1, 5.00, 2, 1);  -- operateur='>'(2), resolution='MAX'(1)

INSERT INTO Parametre (id_matiere, diff, id_operateur, id_resolution) 
VALUES (1, 2.00, 1, 2);  -- operateur='<'(1), resolution='MIN'(2)

INSERT INTO Parametre (id_matiere, diff, id_operateur, id_resolution) 
VALUES (1, 3.00, 3, 3);  -- operateur='='(3), resolution='MOYENNE'(3)