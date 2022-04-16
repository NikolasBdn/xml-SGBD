import java.io.File;
import java.sql.ResultSet;
import java.util.regex.Pattern;

public class Maj extends Requete{

    public Maj(String xmlFileName) {
        super(xmlFileName, "maj.dtd");
    }

    @Override
    public String xmlToSql() {
        Parser parser = new Parser(xmlFileName);
        String tableString = parser.getTagString("TABLE");
        String champString = parser.getTagString("CHAMP");
        String valueString = parser.getTagString("VALUE");
        String conditionString = parser.getTagString("CONDITION");
        
        // la valeur est nombre ou une chaine de caraters
        Pattern intergerPattern = Pattern.compile("\\d+$");
        if (!intergerPattern.matcher(valueString).matches()) {
            valueString = "'" + valueString + "'";
        }   

        String sqlRequest = "UPDATE " + tableString + " SET " + champString + " = " +
            valueString + " WHERE " + conditionString + ";";

        return sqlRequest;
    }

    @Override
    public File sqlResponseToXML(ResultSet rs) {
        return null;
    }
    
}
