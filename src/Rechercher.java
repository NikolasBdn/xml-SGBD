import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class Rechercher extends Requete {

  public Rechercher(String xmlFileName) {
    super(xmlFileName, "recherche.dtd");
  }

  // Convertion en SQL
  public String xmlToSql() {
    Parser parser = new Parser(xmlFileName);
    String sqlRequest = "Select ";
    // Recup les CHAMPS
    ArrayList<String> champsList = parser.getChildsTagList("CHAMPS");
    for (int i = 0; i < champsList.size(); i++) {
      String champs = champsList.get(i);
      System.out.println(champs);
      if (i == 0) {
        sqlRequest += champs;
      } else {
        sqlRequest += ", " + champs;
      }
    }
    // Recup les TABLES
    sqlRequest += " FROM ";
    ArrayList<String> tableList = parser.getChildsTagList("TABLES");
    for (int i = 0; i < tableList.size(); i++) {
      String table = tableList.get(i);
      if (i == 0) {
        sqlRequest += table;
      } else {
        sqlRequest += " NATURAL JOIN " + table;
      }
    }
    // Recup les CONDITIONS
    String conditionString = parser.getTagString("CONDITION");
    if (!conditionString.equals("")) {
      sqlRequest += " WHERE " + conditionString + ";";
    }

    return sqlRequest;
  }

  public File sqlResponseToXML(ResultSet rs) {
    String[] xmlFileNameTab = this.xmlFileName.split("[.]");
    String responseFileName = xmlFileNameTab[0] + "-response.xml";
    ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
    try {
      ResultSetMetaData rsmd = rs.getMetaData();
      // getting the column type
      int column_count = rsmd.getColumnCount();
      // System.out.println("column_count: " + column_count);

      while (rs.next()) {
        ArrayList<String> tuple = new ArrayList<String>();
        for (int i = 1; i < column_count + 1; i++) {
          // System.out.println(rs.getString(i));
          tuple.add(rs.getString(i));
        }
        data.add(tuple);
        // System.out.println("");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    Parser parser = new Parser(responseFileName);
    parser.createResponseRecherche(data, responseFileName);

    //Signature du fichier XML de reponse
    SignatureVerifXML signatureVerifXML = new SignatureVerifXML(responseFileName);
    signatureVerifXML.signed();
    System.out.println("Signature de la reponse valide ? :" + signatureVerifXML.verifSignature());

    return new File(xmlFileName);
  }

  @Override
  public ResultSet executSQL() {
    return null;
  }
}
