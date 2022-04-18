import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Inserer extends Requete {

    public Inserer(String xmlFileName) {
        super(xmlFileName, "inserer.dtd");
    }

    /**
     * Retourne les valeurs du i eme tuple a inserer
     * 
     * @param i
     * @return ArrayList<String>
     */
    public ArrayList<String> getValues(int i) {
        ArrayList<String> valuersTab = new ArrayList<String>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = factory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(this.xmlFileName));

            Element node = (Element) doc.getElementsByTagName("VALUES").item(i);
            NodeList nList = node.getElementsByTagName("VALUE");

            for (int j = 0; j < nList.getLength(); j++) {
                Node value = nList.item(j);
                valuersTab.add(value.getTextContent());
            }
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return valuersTab;
    }

    /**
     * Convertit le fichier xml en requete sql.
     * 
     * @return String
     */
    public String xmlToSql() {
        Parser parser = new Parser(xmlFileName);
        String sqlRequest = "";
        int valuesNumber = parser.countElement("VALUES");

        for (int i = 0; i < valuesNumber; i++) {

            sqlRequest += "INSERT INTO ";
            // Recup TABLE
            String tableString = parser.getTagString("TABLE");

            if (!tableString.equals("")) {
                sqlRequest += tableString + " VALUES (";
            }

            Pattern intergerPattern = Pattern.compile("\\d+$");
            Pattern datePattern = Pattern.compile("^(3[01]"
                    + "|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$");

            ArrayList<String> valuesList = this.getValues(i);

            for (int j = 0; j < valuesList.size(); j++) {
                String value = valuesList.get(j);

                // Si la valeur contien au moin une lettre alors cest un varchar en SQL
                if (datePattern.matcher(value).matches()) {
                    value = "str_to_date('" + value + "','%d/%m/%Y')";
                } else if (!intergerPattern.matcher(value).matches()) {
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

    /**
     * Convertit la reponse sql en fichier xml.
     * 
     * @param rs
     * @return File
     */
    public File sqlResponseToXML(ResultSet rs) {
        return null;
    }
}
