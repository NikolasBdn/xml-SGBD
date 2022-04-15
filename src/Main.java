import java.sql.*;
import java.io.File;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {

    // BDD Connection
    MysqlCon con = new MysqlCon();
    // ResultSet rs = con.doRequest("select * from equipe;");
    // try {
    // while(rs.next()) {
    // System.out.println(rs.getString("codeEquipe") + ", " + rs.getString("pays"));
    // }
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }

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
      if (choix.equals("R") || choix.equals("I") || choix.equals("M") || choix.equals("E") || choix.equals("Q")) {
        choixCorrect = true;
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

    // String[] tabLienFichierXml = lienFichierXML.split("/");
    // String nomFichierXml = tabLienFichierXml[tabLienFichierXml.length - 1];
    String sqlReqString = "";
    ResultSet rs = null;
    Requete requete = null;
    
    // if (choix.equals("R") || choix.equals("I") ||choix.equals("M") ||choix.equals("E") || choix.equals("Q")) {

    // }

    switch (choix) {
      case "R":
        requete = new Rechercher(lienFichierXML);
        break;
      case "I":
        requete = new Inserer(lienFichierXML);
        // SignatureVerifXML signatrue = new SignatureVerifXML(lienFichierXML);
        // signatrue.signed();
        // System.out.println("verifed signature: " + signatrue.verifSignature());
        break;
      case "M":
        requete = new Maj(lienFichierXML);
        break;
      case "E":
        break;
      case "Q":
        break;
    } 
    // SignatureVerifXML signatureVerifXML = new SignatureVerifXML(lienFichierXML);
    // signatureVerifXML.signed();
    if (requete.verifSignature() && requete.verifDTD()) {
      // Converte XML request to SQL
      sqlReqString = requete.xmlToSql();
      System.out.println(sqlReqString);
      
      // Execut SQL request    
      if (choix.equals("R")) {
        // Creation du fichier XML de reponse 
        rs = con.doRequest(sqlReqString);
        requete.sqlResponseToXML(rs);
      } else {
        con.doStatement(sqlReqString);
      } 
    }
    in.close();
  }
}
