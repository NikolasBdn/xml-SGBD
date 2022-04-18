import java.io.File;
import java.sql.ResultSet;

public abstract class Requete {
    protected String xmlFileName;
    public String dtdFileName;

    public Requete(String xmlFileName, String dtdFileName) {
        this.xmlFileName = xmlFileName;
        this.dtdFileName = dtdFileName;
    }

    /**
     * Verifie que le fichier xml soit bien signe
     * 
     * @return boolean
     */
    public boolean verifSignature() {
        // Veriff de la signature
        SignatureVerifXML signature = new SignatureVerifXML(this.xmlFileName);
        // signature.signed();
        boolean resSignatureValidation = signature.verifSignature();
        System.out.println("Vérification de la signature de " + this.xmlFileName +
                ": " + resSignatureValidation);

        return resSignatureValidation;
    }

    /**
     * Verifie que le fichier xmlFileName correspond a la DTD du fichier dtdFileName
     * 
     * @return boolean
     */
    public boolean verifDTD() {
        Parser parser = new Parser(this.xmlFileName);
        boolean resDtdValisation = parser.validateWithExtDTDUsingDOM(this.xmlFileName, this.dtdFileName);

        System.out.println("Vérification de la DTD de " + this.xmlFileName +
                " pour une requete INSERER: " + resDtdValisation + "\n");

        return resDtdValisation;
    }

    /**
     * Convertit le fichier xml en requete sql.
     * 
     * @return String
     */
    public abstract String xmlToSql();

    /**
     * Convertit la reponse sql en fichier xml.
     * 
     * @param rs
     * @return File
     */
    public abstract File sqlResponseToXML(ResultSet rs);
}