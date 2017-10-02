package assignment3.model;

import java.io.File;
import java.util.HashSet;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.base.Strings;

import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;
import assignment3.model.Model;

public class ModelManager implements Model {

    private final String SAVED_LOCATION = "Dataset/";
    private final String XML_FORMAT = ".xml";

    @Override
    public void saveJournalData(List<SerializedJournal> journalList, String conferenceName) throws Exception{
        writeToXmlFile(journalList, conferenceName);
    }

    @Override
    public List<SerializedJournal> getJournalData(String conferenceName) {
        return null;
    }

    private void writeToXmlFile(List<SerializedJournal> journalList, String conferenceName) throws Exception {
        int totalNoOfAuthor =  0;
        int totalNoOfCitation = 0;
        int noOfJournal = 0;
        int lowestYear = 9999;
        int highestYear = 0;
        HashSet<SerializedJournal> journalSet = new HashSet<>();
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder xmlBuilder = builderFactory.newDocumentBuilder();
        Document doc = xmlBuilder.newDocument();

        Element rootElement = doc.createElement(conferenceName);
        doc.appendChild(rootElement);

        Element parshed = doc.createElement("algorithm");
        parshed.setAttribute("name", "ParsHed");
        Element parschit = doc.createElement("algorithm");
        parschit.setAttribute("name", "ParsCit");
        Element citationslists = doc.createElement("citationlists");
        parschit.appendChild(citationslists);
        Element authorElement = doc.createElement("NoOfAuthor");
        Element noOfcitationElement = doc.createElement("noOfCitation");
        Element yearRangeElement = doc.createElement("yearRange");
        Element noOfJournalElement = doc.createElement("noOfJournal");
        rootElement.appendChild(authorElement);
        rootElement.appendChild(noOfcitationElement);
        rootElement.appendChild(yearRangeElement);
        rootElement.appendChild(parshed);
        rootElement.appendChild(parschit);
        
        noOfJournal+= journalList.size();
        for (SerializedJournal journal : journalList) {
            if (!journalSet.contains(journal)) {
                Element journalElement = doc.createElement("journal");
                appendChildToELement("title", journal.title, journalElement, doc);
                appendChildToELement("authir", journal.author, journalElement, doc);
                appendChildToELement("affiliation", journal.affiliation, journalElement, doc);
                appendChildToELement("abstractText", journal.abstractText, journalElement, doc);
                parshed.appendChild(journalElement);
                journalSet.add(journal);
            }
            totalNoOfCitation+= journal.citations.size();

            for (SerializedCitation citation : journal.citations) {
                Element citationElement = doc.createElement("citation");
                citationElement.setIdAttribute("value", true);
                appendChildToELement("title", citation.title, citationElement, doc);
                appendChildToELement("booktitle", citation.booktitle, citationElement, doc);
                if (citation.year != 0) {
                    if (citation.year < lowestYear) {
                        lowestYear = citation.year;
                    }
                    if (citation.year > highestYear) {
                        highestYear = citation.year;
                    }
                    appendChildToELement("year", Integer.toString(citation.year), citationElement, doc);
                }

                totalNoOfAuthor+= citation.authorsList.size();

                for(String citation_author : citation.authorsList) {
                    appendChildToELement("author", citation_author, citationElement, doc);
                }
                citationslists.appendChild(citationElement);
            }
        }

        noOfcitationElement.appendChild(doc.createTextNode(Integer.toString(totalNoOfCitation)));
        authorElement.appendChild(doc.createTextNode(Integer.toString(totalNoOfAuthor)));
        noOfJournalElement.appendChild(doc.createTextNode(Integer.toString(noOfJournal)));
        String yearRange = lowestYear + " to " + highestYear;
        yearRangeElement.appendChild(doc.createTextNode(yearRange));
        String outputLocation = SAVED_LOCATION + conferenceName + XML_FORMAT;
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(outputLocation));
        transformer.transform(source,  result);


    }
    private static void appendChildToELement(String elementName, String elementValue,
                                             Element element, Document doc) {
        if (!Strings.isNullOrEmpty(elementValue)) {
            Element child = doc.createElement(elementName);
            child.appendChild(doc.createTextNode(elementValue));
            element.appendChild(child);
        }

    }
}
