# xml-SGBD

Compiler: 
javac *.java

Executer:
java Main

Utilisation:
```
> java Main  
 - (R)echercher
 - (I)nsérer
 - (M)ettre à jour
 - (E)ffacer
 - (Q)uitter
```

Enterer R, I, M ou E en fonction de l action voulu et Q pour quitter.
```
R
Entrer le lien vers votre fichier XML:
recherche.xml
```

Donner le chemin vers le fichier XML à utiliser.
```

Vérification de la signature de recherche.xml: true
Vérification de la DTD de recherche.xml pour une requete INSERER: true

Requetes generees depuis le fichier XML : 
  Select nom, prenom, numJoueur, numero FROM equipe NATURAL JOIN joueur WHERE codeEquipe = "FRA";

connexion a la base de données
connexion réussie

Signature de la reponse valide ? :true
```

Pour voir la reponse 

```
cat response-recherche.xml
...
```
