package mpegg.gcs.genomiccontentservice.Utils;

import mpegg.gcs.genomiccontentservice.Models.Dataset;
import mpegg.gcs.genomiccontentservice.Models.DatasetGroup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
        Element root = d.getDocumentElement();

        if (root.getElementsByTagName("Title").getLength() != 0) dg.setTitle(root.getElementsByTagName("Title").item(0).getTextContent());
        if (root.getElementsByTagName("Description").getLength() != 0) dg.setDescription(root.getElementsByTagName("Description").item(0).getTextContent());
        if (root.getElementsByTagName("Type").getLength() != 0) dg.setType(root.getElementsByTagName("Type").item(0).getTextContent());
        if (root.getElementsByTagName("ProjectCentreName").getLength() != 0) dg.setCenter(root.getElementsByTagName("ProjectCentreName").item(0).getTextContent());
        return dg;
    }

    public Dataset parseDataset(String xml, Dataset dt) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document d = builder.parse(is);
        Element root = d.getDocumentElement();
        NodeList list = root.getElementsByTagName("Sample");
        if (list.getLength() != 0) {
            Element sample = (Element)list.item(0);
            if (sample.getElementsByTagName("Title").getLength() != 0) dt.setTitle(sample.getElementsByTagName("Title").item(0).getTextContent());
            if (sample.getElementsByTagName("TaxonId").getLength() != 0) dt.setTaxon_id(sample.getElementsByTagName("TaxonId").item(0).getTextContent());
        }
        return dt;
    }
}

