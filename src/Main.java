import java.sql.*;
import java.io.File;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {

    // BDD Connection
    MysqlCon con = new MysqlCon();

    // Menu
    Scanner in = new Scanner(System.in);
    String lienFichierXML = "";
    String choix = "";
    boolean choixCorrect = false;
    // Tant que le choix d'action n'est pas correct
    while (!choixCorrect) {
      System.out.println(" - (R)echercher");
      System.out.println(" - (I)nsérer");
      System.out.println(" - (M)ettre à jour");
      System.out.println(" - (E)ffacer");
      System.out.println(" - (Q)uitter");
      choix = in.nextLine();
      if (choix.equals("R") || choix.equals("I") || choix.equals("M") || choix.equals("E")) {
        choixCorrect = true;
        // Q pour quitter
      } else if (choix.equals("Q")) {
        System.exit(0);
      }
      // Tant que le lien du fichier XML n'est pas correct
      boolean fichierCorrect = false;
      while (!fichierCorrect) {
        System.out.println("Entrer le lien vers votre fichier XML:");
        lienFichierXML = in.nextLine();
        File file = new File(lienFichierXML);
        if (file.exists()) {
          fichierCorrect = true;
        } else {
          System.out.println("Erreur: impossible de trouver le fichier à l'emplacement: " + lienFichierXML);
        }
      }
    }

    String sqlReqString = "";
    ResultSet rs = null;
    Requete requete = null;
    
    // Creation de la requete en fonction de l action
    switch (choix) {
      case "R":
        requete = new Rechercher(lienFichierXML);
        break;
      case "I":
        requete = new Inserer(lienFichierXML);
        break;
      case "M":
        requete = new Maj(lienFichierXML);
        break;
      case "E":
        requete = new Effacer(lienFichierXML);
        break;
    }

    // SignatureVerifXML signatureVerifXML = new SignatureVerifXML(lienFichierXML);
    // signatureVerifXML.signed();
    if (requete.verifSignature() && requete.verifDTD()) {
      // Converte XML request to SQL
      sqlReqString = requete.xmlToSql();
      System.out.println("Requetes generees depuis le fichier XML : ");
      String[] requestsTab = sqlReqString.split("\n");
      for (String r : requestsTab) {
        System.out.println("  " + r);
      }
      System.out.println();
      // Execut SQL request
      if (choix.equals("R")) {
        //Execution de la requete SQL de type SELECT
        rs = con.doRequest(sqlReqString);
        // Creation du fichier XML de reponse
        requete.sqlResponseToXML(rs);
      } else {
        //Execution de la requete SQL de type UPDATE, DELETE et INSERT
        con.doStatement(sqlReqString);
      }
    }
    in.close();
  }
}
