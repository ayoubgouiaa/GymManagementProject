CREATE DATABASE GymManagement;
USE GymManagement;
CREATE TABLE ABONNEMENT (
id_abonnement INT AUTO_INCREMENT PRIMARY KEY,
nom_formule VARCHAR(100) NOT NULL,
duree_mois INT NOT NULL,
prix DECIMAL(10,2) NOT NULL,
nb_seances_semaine INT NOT NULL,
description TEXT
);
CREATE TABLE MEMBRE (
    id_membre INT AUTO_INCREMENT PRIMARY KEY,
    numero_membre VARCHAR(20) ,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    date_naissance DATE,
    telephone VARCHAR(15),
    email VARCHAR(100),
    id_abonnement INT,
    date_fin_abonnement DATE,
    statut VARCHAR(50),
    FOREIGN KEY (id_abonnement) REFERENCES ABONNEMENT(id_abonnement)
);
CREATE TABLE COACH (
    id_coach INT AUTO_INCREMENT PRIMARY KEY,
    matricule VARCHAR(50) NOT NULL,
    nom VARCHAR(50) NOT NULL,	
    prenom VARCHAR(50) NOT NULL,
    specialite VARCHAR(100),
    telephone VARCHAR(15),
    email VARCHAR(100),
    tarif_seance DECIMAL(10,2)
);
CREATE TABLE SEANCE (
    id_seance INT AUTO_INCREMENT PRIMARY KEY,
    nom_seance VARCHAR(100) NOT NULL,
    type_activite VARCHAR(50),
    id_coach INT,
    date_seance DATE,
    heure_debut TIME,
    heure_fin TIME,
    capacite_max INT,
    nb_inscrits INT,
    FOREIGN KEY (id_coach) REFERENCES COACH(id_coach)
);
CREATE TABLE EQUIPEMENT (
    id_equipement INT AUTO_INCREMENT PRIMARY KEY,
    nom_equipement VARCHAR(100),
    categorie VARCHAR(50),
    quantite INT,
    etat VARCHAR(50),
    date_achat DATE,
    cout_maintenance DECIMAL(10,2)
);
SHOW TABLES;	
DROP TABLE MEMBRE;
SELECT * FROM MEMBRE;
-- Insertion dans la table ABONNEMENT
INSERT INTO ABONNEMENT (nom_formule, duree_mois, prix, nb_seances_semaine, description)
VALUES ('Formule Basique', 12, 199.99, 3, 'Accès aux équipements et 3 séances de groupe par semaine');

-- Insertion dans la table MEMBRE
INSERT INTO MEMBRE (numero_membre, nom, prenom, date_naissance, telephone, email, id_abonnement, date_fin_abonnement, statut)
VALUES ('M12345', 'Dupont', 'Jean', '1990-05-15', '123456789', 'jean.dupont@example.com', 1, '2026-05-15', 'Actif');

-- Insertion dans la table COACH
INSERT INTO COACH (matricule, nom, prenom, specialite, telephone, email, tarif_seance)
VALUES ('C001', 'Martin', 'Pierre', 'Yoga', '987654321', 'pierre.martin@example.com', 50.00);

-- Insertion dans la table SEANCE
INSERT INTO SEANCE (nom_seance, type_activite, id_coach, date_seance, heure_debut, heure_fin, capacite_max, nb_inscrits)
VALUES ('Séance Yoga', 'Yoga', 1, '2025-12-20', '08:00:00', '09:00:00', 20, 10);

-- Insertion dans la table EQUIPEMENT
INSERT INTO EQUIPEMENT (nom_equipement, categorie, quantite, etat, date_achat, cout_maintenance)
VALUES ('Tapis de Yoga', 'Matériel de yoga', 10, 'Bon', '2025-01-15', 50.00);
Select * FROM membre;

