-- ===================================
-- CRÉATION DE LA BASE
-- ===================================
DROP DATABASE IF EXISTS correcteur_etudiant;
CREATE DATABASE correcteur_etudiant;
\c correcteur_etudiant;

-- ===================================
-- TABLES
-- ===================================

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

-- Supprimer l'ancien type et table si existants
DROP TABLE IF EXISTS Operateur CASCADE;
DROP TYPE IF EXISTS type_operateur;

-- Type ENUM pour opérateurs
CREATE TYPE type_operateur AS ENUM ('<', '>', '<=', '>=', '==');

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

CREATE TABLE Notefinale(
     id SERIAL PRIMARY KEY,
     id_etudiant INT REFERENCES Etudiant(id) NOT NULL,
     id_note INT REFERENCES Note(id) NOT NULL
);

-- ===================================
-- RÉINITIALISATION COMPLÈTE
-- ===================================
SET session_replication_role = 'replica';

TRUNCATE TABLE Notefinale RESTART IDENTITY CASCADE;
TRUNCATE TABLE Parametre RESTART IDENTITY CASCADE;
TRUNCATE TABLE Note RESTART IDENTITY CASCADE;
TRUNCATE TABLE Resolution RESTART IDENTITY CASCADE;
TRUNCATE TABLE Operateur RESTART IDENTITY CASCADE;
TRUNCATE TABLE Correcteur RESTART IDENTITY CASCADE;
TRUNCATE TABLE Etudiant RESTART IDENTITY CASCADE;
TRUNCATE TABLE Matiere RESTART IDENTITY CASCADE;

SET session_replication_role = 'origin';

-- ===================================
-- DONNÉES
-- ===================================

-- Matières
INSERT INTO Matiere (nom, coeff) VALUES ('java', 1);  -- id=1
INSERT INTO Matiere (nom, coeff) VALUES ('php', 1);  -- id=1


-- Étudiants
INSERT INTO Etudiant (nom) VALUES ('candidat1');     -- id=1
INSERT INTO Etudiant (nom) VALUES ('candidat2');  

-- Correcteurs
INSERT INTO Correcteur (nom) VALUES ('correcteur1');   -- id=1
INSERT INTO Correcteur (nom) VALUES ('correcteur2');  -- id=2
INSERT INTO Correcteur (nom) VALUES ('correcteur3');     -- id=3

-- Opérateurs
INSERT INTO Operateur (operateur) VALUES ('<');   -- id=1
INSERT INTO Operateur (operateur) VALUES ('<=');  -- id=5
INSERT INTO Operateur (operateur) VALUES ('>');   -- id=2
INSERT INTO Operateur (operateur) VALUES ('>=');  -- id=4
INSERT INTO Operateur (operateur) VALUES ('=');   -- id=3

INSERT INTO Operateur (operateur) VALUES ('==');  -- id=6

-- Résolutions
INSERT INTO Resolution (resolution) VALUES ('MAX');      -- id=1
INSERT INTO Resolution (resolution) VALUES ('MIN');      -- id=2
INSERT INTO Resolution (resolution) VALUES ('MOYENNE');  -- id=3
INSERT INTO Resolution (resolution) VALUES ('SOMME');    -- id=4

-- ===================================
-- NOTES (Exemples)
-- ===================================


-- ===================================
-- PARAMÈTRES (Exemple pour chaque opérateur)
-- ===================================

INSERT INTO Parametre (id_matiere, diff, id_operateur, id_resolution) VALUES (1, 3.00, 1, 1);

-- '<' : différence inférieure
INSERT INTO Parametre (id_matiere, diff, id_operateur, id_resolution) VALUES (1, 3.00, 4, 3);

-- '=' : différence exacte
INSERT INTO Parametre (id_matiere, diff, id_operateur, id_resolution) VALUES (2, 2.00, 2, 2);

-- '>=' : différence supérieure ou égale
INSERT INTO Parametre (id_matiere, diff, id_operateur, id_resolution) VALUES (2, 2.00, 3, 1);



INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) VALUES (1, 12, 1, 1);
INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) VALUES (1, 11, 2, 1);


-- EXEMPLE 2: Petites différences
INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) VALUES (2, 7.0, 1, 1);
INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) VALUES (2, 11.0, 2, 1);


-- EXEMPLE 3: Grandes différences
INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) VALUES (1, 13.0, 1, 2);
INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) VALUES (1, 10.0, 2, 2);


-- EXEMPLE 4: Différence exacte
INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) VALUES (2, 14.0, 1, 2);
INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) VALUES (2, 16.0, 2, 2);





