package assignment3.model;

import java.io.File;
import java.util.HashMap;
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
import assignment3.storage.Storage;
import assignment3.storage.StorageManager;
import javafx.util.Pair;

public class ModelManager implements Model {

    private final String SAVED_LOCATION = "Dataset/";
    private final String XML_FORMAT = ".xml";
    private final Storage storage;
    private final HashMap<String, HashMap<Integer, SerializedJournal>> journalMap;
    private final HashMap<String, HashMap<Integer, List<SerializedCitation>>> citationMap;
    public ModelManager () {
    	journalMap = new HashMap<>();
    	citationMap = new HashMap<>();
    	storage = new StorageManager();
    }
    @Override
    public void saveJournalData(List<SerializedJournal> journalList, String conferenceName) throws Exception{
        writeToXmlFile(journalList, conferenceName);
    }


	@Override
	public HashMap<Integer, SerializedJournal> getJournal(String conferenceName) throws Exception {
		if (journalMap.containsKey(conferenceName)) {
			return journalMap.get(conferenceName);
		}
		getJournalData(conferenceName);
		return journalMap.get(conferenceName);
	}

	@Override
	public HashMap<Integer, List<SerializedCitation>> getCitations(String conferenceName) throws Exception {
		if (citationMap.containsKey(conferenceName)) {
			return citationMap.get(conferenceName);
		}
		getJournalData(conferenceName);
		return citationMap.get(conferenceName);
	}
	private void getJournalData(String conferenceName) throws Exception {
		Pair<HashMap<Integer, SerializedJournal>, HashMap<Integer, List<SerializedCitation>>> pair = storage.retrieveFile(conferenceName);
		journalMap.put(conferenceName, pair.getKey());
		citationMap.put(conferenceName, pair.getValue());
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

        Element mainElement = doc.createElement("main");
        Element citationslists = doc.createElement("citationLists");
    
        Element authorElement = doc.createElement("NoOfAuthor");
        Element noOfcitationElement = doc.createElement("noOfCitation");
        Element yearRangeElement = doc.createElement("yearRange");
        Element noOfJournalElement = doc.createElement("noOfJournal");
        rootElement.appendChild(mainElement);
        rootElement.appendChild(citationslists);
        rootElement.appendChild(authorElement);
        rootElement.appendChild(noOfcitationElement);
        rootElement.appendChild(yearRangeElement);
        rootElement.appendChild(noOfJournalElement);

        
        noOfJournal+= journalList.size();
        int id = 1;
        for (SerializedJournal journal : journalList) {
            if (!journalSet.contains(journal)) {
            	Element journalElement = doc.createElement("journal");
            	journalElement.setAttribute("id" , Integer.toString(id));
                appendChildToELement("title", journal.title, journalElement, doc);
                appendChildToELement("author", journal.author, journalElement, doc);
                appendChildToELement("affiliation", journal.affiliation, journalElement, doc);
                appendChildToELement("abstractText", journal.abstractText, journalElement, doc);
                mainElement.appendChild(journalElement);
                journalSet.add(journal);
            }
            totalNoOfCitation+= journal.citations.size();
            for (SerializedCitation citation : journal.citations) {
                Element citationElement = doc.createElement("citation");

                Element authorsElement = doc.createElement("authors");
                citationElement.setAttribute("id", Integer.toString(id));
                if (!citation.authorsList.isEmpty()) {
                    citationElement.appendChild(authorsElement);
                }
                appendChildToELement("title", citation.title, citationElement, doc);
                appendChildToELement("booktitle", citation.booktitle, citationElement, doc);
                if (citation.year != 0) {
                    if (citation.year < lowestYear) {
                        lowestYear = citation.year;
                    }
                    if (citation.year > highestYear) {
                        highestYear = citation.year;
                    }
                    appendChildToELement("date", Integer.toString(citation.year), citationElement, doc);
                }

                totalNoOfAuthor+= citation.authorsList.size();
                for(String citation_author : citation.authorsList) {
                    appendChildToELement("author", citation_author, authorsElement, doc);
                }
                citationslists.appendChild(citationElement);
            }
            id++;
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
	@Override
	public HashMap<String, HashMap<Integer, SerializedJournal>> getJournalMap() {
		return journalMap;
	}
	@Override
	public HashMap<String, HashMap<Integer, List<SerializedCitation>>> getCitationMap() {
		// TODO Auto-generated method stub
		return citationMap;
	}

}
