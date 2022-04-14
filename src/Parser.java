import java.io.File;

import java.io.IOException;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.w3c.dom.Element;

public class Parser {
    private String xmlFileName;
    private File xmlFile;
    private DocumentBuilderFactory docBuilderFactory;

    public Parser(String xmlFileName) {
        this.xmlFileName = xmlFileName;
        this.xmlFile = new File(xmlFileName);
        docBuilderFactory = DocumentBuilderFactory.newInstance();
    }

    public boolean validateWithExtDTDUsingDOM(String xml, String dtd) {
        boolean valid = true;
        // DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = docBuilderFactory.newDocumentBuilder();

            Document doc = db.parse(this.xmlFile);
            DOMSource source = new DOMSource(doc);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtd);
            // StringWriter writer = new StringWriter();
            // StreamResult result = new StreamResult(writer);
            transformer.transform(source, new StreamResult(new File(this.xmlFileName)));

            docBuilderFactory.setValidating(true);

            DocumentBuilder dBuilder;

            dBuilder = docBuilderFactory.newDocumentBuilder();

            dBuilder.setErrorHandler(new ErrorHandler() {

                @Override
                public void error(SAXParseException exception) throws SAXException {
                    System.out.println("DTD validation ERROR: line " +
                            exception.getLineNumber() + ", column " + exception.getColumnNumber()
                            + ": " +
                            exception.getLocalizedMessage());
                    throw new SAXException();
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    // System.out.println("FATAL ERROR");
                    // System.out.println("DTD validation ERROR: line " +
                    // exception.getLineNumber() + ", column " + exception.getColumnNumber()
                    // + ": " +
                    // exception.getLocalizedMessage());
                    throw new SAXException();
                }

                @Override
                public void warning(SAXParseException exception) throws SAXException {
                    System.out.println("WARNING");
                    throw new SAXException();
                }
            });

        } catch (SAXException e) {
            e.printStackTrace();
            valid = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return valid;
    }

    public int countElement(String elementName) {
        int count = 0;
        try {
        DocumentBuilder db = docBuilderFactory.newDocumentBuilder();

        Document doc;
        doc = db.parse(this.xmlFile);
        NodeList list = doc.getElementsByTagName(elementName);
        count = list.getLength();
        System.out.println("Total of elements : " + list.getLength());
        }
        catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return count;
    }

    
    public ArrayList<String> getChildsTagList(String parent) {
        ArrayList<String> childsList = new ArrayList<String>();
        try {
            DocumentBuilder db;
            db = docBuilderFactory.newDocumentBuilder();

            Document doc;
            doc = db.parse(this.xmlFile);
            NodeList nlParent = doc.getElementsByTagName(parent);
            NodeList nlChilds = nlParent.item(0).getChildNodes();

            for (int i = 0; i < nlChilds.getLength(); i++) {
                Node nNode = nlChilds.item(i);
                // System.out.println("childs node text content: " + nNode.getTextContent());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    childsList.add(nNode.getTextContent().trim());
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return childsList;
    }

    public String getTagString(String tag) {
        String res = "";
        try {
            DocumentBuilder db = docBuilderFactory.newDocumentBuilder();

            Document doc = db.parse(this.xmlFile);
            Node node = doc.getElementsByTagName(tag).item(0);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                res = node.getTextContent().trim();
            }

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return res;
    }

    public void createResponseRecherche(ArrayList<ArrayList<String>> data, String xmlFileName) {
        try {
            DocumentBuilder db = docBuilderFactory.newDocumentBuilder();
            Document doc = db.newDocument();

            Element rootElement = doc.createElement("RESULTAT");

            for (ArrayList<String> tuple : data) {
                Element tuplesElement = doc.createElement("TUPLES");
                rootElement.appendChild(tuplesElement);

                for (String champs : tuple) {
                    // System.out.println(champs);
                    Element tupleElement = doc.createElement("TUPLE");
                    Text champsElement = doc.createTextNode(champs);

                    tuplesElement.appendChild(tupleElement);
                    tupleElement.appendChild(champsElement);
                }
            }

            doc.appendChild(rootElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer;

            transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            StreamResult console = new StreamResult(System.out);
            StreamResult file = new StreamResult(new File(xmlFileName));

            transformer.transform(source, console);

            transformer.transform(source, file);
            System.out.println("DONE");

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
