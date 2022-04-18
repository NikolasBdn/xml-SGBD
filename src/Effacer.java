import java.io.File;
import java.sql.ResultSet;

public class Effacer extends Requete {

    /**
     * Constructeur de la class Effacer qui hérite de Requete
     * @param xmlFileName
     */
    public Effacer(String xmlFileName) {
        super(xmlFileName, "effacer.dtd");
    }

    
    /** 
     * Convertit le fichier xml en requete sql pour effacer.
     * @return String
     */
    @Override
    public String xmlToSql() {
        Parser parser = new Parser(xmlFileName);

        String tableString = parser.getTagString("TABLE");
        String conditionString = parser.getTagString("CONDITION");

        String sqlRequest = "DELETE FROM " + tableString + " WHERE " + conditionString + ";";

        return sqlRequest;
    }

    
    /** 
     * Convertit la reponse sql en xml
     * @param rs
     * @return File
     */
    @Override
    public File sqlResponseToXML(ResultSet rs) {
        return null;
    }

}
