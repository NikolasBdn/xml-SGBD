import java.io.File;
import java.sql.ResultSet;

public class Effacer extends Requete {

    public Effacer(String xmlFileName) {
        super(xmlFileName, "effacer.dtd");
    }

    @Override
    public String xmlToSql() {
        Parser parser = new Parser(xmlFileName);

        String tableString = parser.getTagString("TABLE");
        String conditionString = parser.getTagString("CONDITION");

        String sqlRequest = "DELETE FROM " + tableString + " WHERE " + conditionString + ";";

        return sqlRequest;
    }

    @Override
    public File sqlResponseToXML(ResultSet rs) {
        // TODO Auto-generated method stub
        return null;
    }

}
