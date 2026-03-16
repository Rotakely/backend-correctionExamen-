-- ===================================
-- CRÉATION DE LA BASE
-- ===================================
DROP DATABASE IF EXISTS correcteur_etudiant_v2;
CREATE DATABASE correcteur_etudiant_v2;
\c correcteur_etudiant_v2;

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

-- Table Operateur simple (sans type ENUM)
CREATE TABLE Operateur (
    id SERIAL PRIMARY KEY,
    operateur VARCHAR(10) NOT NULL UNIQUE  -- VARCHAR simple au lieu du type ENUM
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
INSERT INTO Matiere (nom, coeff) VALUES ('java', 1);
INSERT INTO Matiere (nom, coeff) VALUES ('php', 1);

-- Étudiants
INSERT INTO Etudiant (nom) VALUES ('candidat1');
INSERT INTO Etudiant (nom) VALUES ('candidat2');

-- Correcteurs
INSERT INTO Correcteur (nom) VALUES ('correcteur1');
INSERT INTO Correcteur (nom) VALUES ('correcteur2');
INSERT INTO Correcteur (nom) VALUES ('correcteur3');

-- Opérateurs (maintenant en VARCHAR simple)
INSERT INTO Operateur (operateur) VALUES ('<');
INSERT INTO Operateur (operateur) VALUES ('<=');
INSERT INTO Operateur (operateur) VALUES ('>');
INSERT INTO Operateur (operateur) VALUES ('>=');
INSERT INTO Operateur (operateur) VALUES ('=');
INSERT INTO Operateur (operateur) VALUES ('==');

-- Résolutions
INSERT INTO Resolution (resolution) VALUES ('MAX');
INSERT INTO Resolution (resolution) VALUES ('MIN');
INSERT INTO Resolution (resolution) VALUES ('MOYENNE');
INSERT INTO Resolution (resolution) VALUES ('SOMME');

-- -- ===================================
-- -- NOTES (Exemples)
-- -- ===================================
-- INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) VALUES (1, 12, 1, 1);
-- INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) VALUES (1, 11, 2, 1);
-- INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) VALUES (2, 7.0, 1, 1);
-- INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) VALUES (2, 11.0, 2, 1);
-- INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) VALUES (1, 13.0, 1, 2);
-- INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) VALUES (1, 10.0, 2, 2);
-- INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) VALUES (2, 14.0, 1, 2);
-- INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) VALUES (2, 16.0, 2, 2);

-- -- ===================================
-- -- PARAMÈTRES
-- -- ===================================
-- INSERT INTO Parametre (id_matiere, diff, id_operateur, id_resolution) VALUES (1, 3.00, 1, 1);
-- INSERT INTO Parametre (id_matiere, diff, id_operateur, id_resolution) VALUES (1, 3.00, 4, 3);
-- INSERT INTO Parametre (id_matiere, diff, id_operateur, id_resolution) VALUES (2, 2.00, 2, 2);
-- INSERT INTO Parametre (id_matiere, diff, id_operateur, id_resolution) VALUES (2, 2.00, 3, 1);



-- 2. PARAMÈTRES (3 seulement)
INSERT INTO Parametre (id_matiere, diff, id_operateur, id_resolution) VALUES 
(1, 2.00, 1, 1),  -- diff=2 → MAX
(1, 4.00, 1, 2),  -- diff=4 → MIN
(1, 6.00, 1, 3);  -- diff=6 → MOYENNE


INSERT INTO Parametre (id_matiere, diff, id_operateur, id_resolution) VALUES 
(1, 2.00, 1, 1),  
(1, 4.00, 1, 2),  
(1, 6.00, 1, 3);  


-- ============================================
-- CAS 1: différence = 1.00 (proche de 2.00)
-- ============================================
INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) VALUES 
(1, 10.0, 1, 1),  -- candidat1
(1, 11.0, 2, 1);  -- note 10 et 11 → différence = 1
-- Attendu: plus proche de 2.00 → MAX

-- ============================================
-- CAS 2: différence = 3.00 (entre 2 et 4)
-- ============================================
INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) VALUES 
(1, 12.0, 1, 1),  -- candidat1
(1, 15.0, 2, 1);  -- note 12 et 15 → différence = 3
-- Attendu: plus proche de 4.00 (distance 1) ou 2.00 (distance 1) ?
-- Réponse: ÉGALITÉ entre 2 et 4 → on prend le PLUS PETIT = 2.00 → MAX

-- ============================================
-- CAS 3: différence = 5.00 (entre 4 et 6)
-- ============================================
INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) VALUES 
(1, 13.0, 1, 2),  -- candidat2
(1, 18.0, 2, 2);  -- note 13 et 18 → différence = 5
-- Attendu: plus proche de 4.00 (distance 1) ou 6.00 (distance 1) ?
-- Réponse: ÉGALITÉ entre 4 et 6 → on prend le PLUS PETIT = 4.00 → MIN

-- ============================================
-- CAS 4: différence = 6.00 (exact)
-- ============================================
INSERT INTO Note (id_matiere, note, id_correcteur, id_etudiant) VALUES 
(1, 14.0, 1, 2),  -- candidat2
(1, 20.0, 2, 2);  -- note 14 et 20 → différence = 6
-- Attendu: correspond exact à 6.00 → MOYENNE