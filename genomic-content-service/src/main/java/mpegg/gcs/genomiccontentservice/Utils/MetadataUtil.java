package mpegg.gcs.genomiccontentservice.Utils;

import mpegg.gcs.genomiccontentservice.Models.DatasetGroup;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

public class MetadataUtil {
    public DatasetGroup parseDatasetGroup(String xml, DatasetGroup dg) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document d = builder.parse(is);
        if (d.getDocumentElement().getElementsByTagName("Title").getLength() != 0) dg.setTitle(d.getDocumentElement().getElementsByTagName("Title").item(0).getTextContent());
        if (d.getDocumentElement().getElementsByTagName("Description").getLength() != 0) dg.setDescription(d.getDocumentElement().getElementsByTagName("Description").item(0).getTextContent());
        if (d.getDocumentElement().getElementsByTagName("Type").getLength() != 0) dg.setType(d.getDocumentElement().getElementsByTagName("Type").item(0).getTextContent());
        if (d.getDocumentElement().getElementsByTagName("ProjectCentre").getLength() != 0) {
            Node a = d.getDocumentElement().getElementsByTagName("ProjectCentre").item(0);
            Node b = a.getFirstChild();
            dg.setCenter(b.getTextContent());
        }
        return dg;
    }
}

