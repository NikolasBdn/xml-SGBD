import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Inserer extends Requete {

    public Inserer(String xmlFileName) {
        super(xmlFileName, "inserer.dtd");
    }

    // Convertion en SQL
    public String xmlToSql() {
        Parser parser = new Parser(xmlFileName);
        String sqlRequest = "";
        // ArrayList<String> valuesList = parser.getChildsTagList("VALUES");
        ArrayList<String> valuesList = parser.getChildsTagList("INSERT");
        int valuesNumber = parser.countElement("VALUES");

        for (int i = 0; i < valuesNumber; i++) {
            sqlRequest += "INSERT INTO ";
            // Recup TABLE

            String tableString = parser.getTagString("TABLE");
            System.out.println("table: " + tableString);
            if (!tableString.equals("")) {
                sqlRequest += tableString + " VALUES (";
            }

            // Au moins une
            Pattern intergerPattern = Pattern.compile("\\d+$");

            String[] valueStringTab = valuesList.get(i + 1).split("\\r?\\n");

            for (int j = 0; j < valueStringTab.length; j++) {
                String value = valueStringTab[j].trim();
                System.out.println("values: " + value);
                // Si la valeur contien au moin une lettre alors cest un varchar en SQL
                if (!intergerPattern.matcher(value).matches()) {
                    value = "'" + value + "'";
                }
                if (j == 0) {
                    sqlRequest += value;
                } else {
                    sqlRequest += ", " + value;
                }
            }
            sqlRequest += ");\n";
        }
        return sqlRequest;
    }

    @Override
    public ResultSet executSQL() {
        return null;
    }

    public File sqlResponseToXML(ResultSet rs) {
        return null;
    }
}
