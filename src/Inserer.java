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

    public ArrayList<String> getValues(int i) {
        ArrayList<String> valuersTab = new ArrayList<String>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = factory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(this.xmlFileName));

            Element node = (Element)doc.getElementsByTagName("VALUES").item(i);
            NodeList nList = node.getElementsByTagName("VALUE");

            System.out.println("nb values: " + nList.getLength());
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

    // Convertion en SQL
    public String xmlToSql() {
        Parser parser = new Parser(xmlFileName);
        String sqlRequest = "";
        int valuesNumber = parser.countElement("VALUES");
        
        for (int i = 0; i < valuesNumber; i++) {

            sqlRequest += "INSERT INTO ";
            // Recup TABLE
            String tableString = parser.getTagString("TABLE");
            System.out.println("table: " + tableString);
            if (!tableString.equals("")) {
                sqlRequest += tableString + " VALUES (";
            }

            Pattern intergerPattern = Pattern.compile("\\d+$");
            ArrayList<String> valuesList = this.getValues(i);
            System.out.println("size: " + valuesList.size());


            for (int j = 0; j < valuesList.size(); j++) {
                String value = valuesList.get(j);
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
