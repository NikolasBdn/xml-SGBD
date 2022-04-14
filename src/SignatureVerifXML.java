import java.io.*;
import java.util.*;
import java.security.*;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import javax.xml.parsers.*;
import javax.xml.crypto.*;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.*;
import javax.xml.crypto.dsig.spec.*;
import javax.xml.crypto.dsig.keyinfo.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.helpers.DefaultHandler;
import org.w3c.dom.*;

public class SignatureVerifXML {
    private String lienFichierXML;

    public SignatureVerifXML(String lienFichierXML) {
        this.lienFichierXML = lienFichierXML;
    }

    public void signed() {
        try {
            File fXmlFile = new File(lienFichierXML);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            // use DTD validation
            dbFactory.setValidating(true);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            // report Errors if DTD validation is On
            // note: ErrorHandler must be defined
            dBuilder.setErrorHandler(new DefaultHandler());
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            // Gen couple cles
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
            kpg.initialize(512);
            KeyPair kp = kpg.generateKeyPair();

            // Creat signing context
            DOMSignContext dsc = new DOMSignContext(kp.getPrivate(), doc.getDocumentElement());

            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

            Reference ref = fac.newReference("", fac.newDigestMethod(DigestMethod.SHA1, null),
                    Collections.singletonList(fac.newTransform(Transform.ENVELOPED,
                            (TransformParameterSpec) null)),
                    null, null);

            SignedInfo si = fac.newSignedInfo(
                    fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS,
                            (C14NMethodParameterSpec) null),
                    fac.newSignatureMethod(SignatureMethod.DSA_SHA1, null),
                    Collections.singletonList(ref));

            KeyInfoFactory kif = fac.getKeyInfoFactory();

            KeyValue kv = kif.newKeyValue(kp.getPublic());
            KeyInfo ki = kif.newKeyInfo(Collections.singletonList(kv));

            XMLSignature signature = fac.newXMLSignature(si, ki);

            signature.sign(dsc);

            // Print result
            OutputStream os;
            os = new FileOutputStream(lienFichierXML);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.transform(new DOMSource(doc), new StreamResult(os));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean verifSignature() {
        boolean res = false;
        try {
            // Verif signiature
            System.out.println(lienFichierXML);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            dbf.setNamespaceAware(true);

            DocumentBuilder builder = dbf.newDocumentBuilder();
            System.out.println("FILE: " + lienFichierXML);
            Document doc2 = builder.parse(new FileInputStream(lienFichierXML));

            NodeList nl = doc2.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
            if (nl.getLength() == 0) {
                throw new Exception("Cannot find Signature element");
            }

            DOMValidateContext valContext = new DOMValidateContext(new KeyValueKeySelector(), nl.item(0));

            XMLSignatureFactory factory = XMLSignatureFactory.getInstance("DOM");

            XMLSignature signature2 = factory.unmarshalXMLSignature(valContext);
            // boolean coreValidity = signature2.validate(valContext);

            res = signature2.getSignatureValue().validate(valContext);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    private static class KeyValueKeySelector extends KeySelector {
        public KeySelectorResult select(KeyInfo keyInfo,
                KeySelector.Purpose purpose,
                AlgorithmMethod method,
                XMLCryptoContext context)
                throws KeySelectorException {
            if (keyInfo == null) {
                throw new KeySelectorException("Null KeyInfo object!");
            }
            SignatureMethod sm = (SignatureMethod) method;
            List<XMLStructure> list = keyInfo.getContent();
            for (int i = 0; i < list.size(); i++) {
                XMLStructure xmlStructure = list.get(i);
                if (xmlStructure instanceof KeyValue) {
                    PublicKey pk = null;
                    try {
                        pk = ((KeyValue) xmlStructure).getPublicKey();
                    } catch (KeyException ke) {
                        throw new KeySelectorException(ke);
                    }

                    // make sure algorithm is compatible with method
                    if (algEquals(sm.getAlgorithm(), pk.getAlgorithm())) {
                        return new SimpleKeySelectorResult(pk);
                    }
                }
            }
            throw new KeySelectorException("No KeyValue element found!");
        }

        static boolean algEquals(String algURI, String algName) {
            if (algName.equalsIgnoreCase("DSA") &&
                    algURI.equalsIgnoreCase(SignatureMethod.DSA_SHA1)) {
                return true;
            } else if (algName.equalsIgnoreCase("RSA") &&
                    algURI.equalsIgnoreCase(SignatureMethod.RSA_SHA1)) {
                return true;
            } else {
                return false;
            }
        }
    }

    private static class SimpleKeySelectorResult implements KeySelectorResult {
        private PublicKey pk;

        SimpleKeySelectorResult(PublicKey pk) {
            this.pk = pk;
        }

        public Key getKey() {
            return pk;
        }
    }

}
