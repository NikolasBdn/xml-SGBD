import java.io.File;
import java.sql.ResultSet;

public abstract class Requete {
    protected String xmlFileName;
    public String dtdFileName;

    public Requete(String xmlFileName, String dtdFileName) {
        this.xmlFileName = xmlFileName;
        this.dtdFileName = dtdFileName;
    }
    
    public boolean verifSignature() {
     // Veriff de la signature
     SignatureVerifXML signature = new SignatureVerifXML(this.xmlFileName);
     // signature.signed();
     boolean resSignatureValidation = signature.verifSignature();
     System.out.println("Vérification de la signature de " + this.xmlFileName +
             ": " + resSignatureValidation);

     return resSignatureValidation;
    }

    public boolean verifDTD() {
        Parser parser = new Parser(this.xmlFileName);
        boolean resDtdValisation = parser.validateWithExtDTDUsingDOM(this.xmlFileName, this.dtdFileName);

        System.out.println("Vérification de la DTD de " + this.xmlFileName +
                " pour une requete INSERER: " + resDtdValisation);

        return resDtdValisation;
    }
    
    public abstract String xmlToSql();

    public abstract File sqlResponseToXML(ResultSet rs);
}