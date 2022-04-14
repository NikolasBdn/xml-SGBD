create table arbitre (
  numArbitre int(11) AUTO_INCREMENT PRIMARY KEY ,
  nomArbitre varchar(50) DEFAULT NULL,
  notionalite varchar(30) DEFAULT NULL
);

create table equipe (
    codeEquipe varchar(4) PRIMARY KEY,
    pays varchar(50) DEFAULT NULL,
    couleur varchar(50) DEFAULT NULL,
    entraineur varchar(50) DEFAULT NULL
);

create table stade (
    numStade int(11) PRIMARY KEY,
    ville varchar(50) DEFAULT NULL,
    nomStade varchar(50) DEFAULT NULL,
    capacite int(11) DEFAULT NULL
);

create table matchs (
  numMatch int(11) PRIMARY KEY AUTO_INCREMENT,
  dateMatch date DEFAULT NULL,
  nbSpect int(11) DEFAULT NULL,
  numStade int(11) DEFAULT NULL,
  codeEquipeR varchar(4) DEFAULT NULL,
  scoreR int(11) DEFAULT NULL,
  nbEssaisR int(11) DEFAULT NULL,
  codeEquipeD varchar(4) DEFAULT NULL,
  scoreD int(11) DEFAULT NULL,
  nbEssaisD int(11) DEFAULT NULL,
  FOREIGN KEY(numStade) REFERENCES stade(numStade),
  FOREIGN KEY(codeEquipeD) REFERENCES equipe(codeEquipe),
  FOREIGN KEY(codeEquipeR) REFERENCES equipe(codeEquipe)
);

create table poste (
  numero int(11) PRIMARY KEY,
  libelle varchar(50) DEFAULT NULL
);

create table joueur (
  numJoueur int(11) PRIMARY KEY AUTO_INCREMENT,
  prenom varchar(50) DEFAULT NULL,
  nom varchar(50) DEFAULT NULL,
  numPoste int(11) DEFAULT NULL,
  equipe varchar(4) DEFAULT NULL,
  FOREIGN KEY(equipe) REFERENCES equipe(codeEquipe),
  FOREIGN KEY(numPoste) REFERENCES poste(numero)
);

create table jouer (
  numMatch int(11),
  numJoueur int(11),
  titulaire varchar(2) DEFAULT NULL,
  tpsJeu int(11) DEFAULT NULL,
  nbPoints int(11) DEFAULT NULL,
  nbEssais int(11) DEFAULT NULL,
  PRIMARY KEY(numMatch, numJoueur),
  FOREIGN KEY(numMatch) REFERENCES matchs(numMatch),
  FOREIGN KEY(numJoueur) REFERENCES joueur(numJoueur)
);

create table arbitrer (
  numMatch int(11),
  numArbitre int(11),
  PRIMARY KEY(numMatch, numArbitre),
  FOREIGN KEY (numMatch) REFERENCES matchs(numMatch),
  FOREIGN KEY (numArbitre) REFERENCES arbitre(numArbitre)
);
