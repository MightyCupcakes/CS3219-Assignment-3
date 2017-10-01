package assignment3.manager;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.base.Strings;

import assignment3.dataparser.xmlparser.XmlDataParser;
import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;
import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.logic.Logic;


public class LogicManager implements Logic{
	private XmlDataParser parser;
	private final String SAVED_LOCATION = "Dataset/";
	private final String XML_FORMAT = ".xml";
	public LogicManager () {
		parser = new XmlDataParser();
	}

	@Override
	public void parseAndSaveRawData(String folder) throws Exception {
		File conferenceFolder = new File(folder);
		String conferenceName  = conferenceFolder.getName();
		List<String> conferenceLists = getListOfConferences(folder);
		List<SerializedJournal> journalList = convertToJournals(conferenceLists);
		writeToXmlFile(journalList, conferenceName);

	}

	@Override
	public List<SerializedJournalCitation> getDataFromTableWithNoCitations(String tableName) {
		return null;
	}

	@Override
	public List<SerializedJournalCitation> getDataFromTableWithCitations(String tableName) {
		return null;
	}

	private List<String> getListOfConferences(String folder) throws Exception {
		return Files.walk(Paths.get(folder))
				.filter(Files::isRegularFile)
				.map(Path::toString)
				.collect(Collectors.toList());
	}
	private List<SerializedJournal> convertToJournals(List<String> conferenceLists) throws Exception{
		List<SerializedJournal> journalList = new ArrayList<>();
		for(String conference : conferenceLists) {
			parser.parseFile(conference);
			journalList.add(parser.getJournal());
		}
		return journalList;
	}

	private void writeToXmlFile(List<SerializedJournal> journalList, String conferenceName) throws Exception {
		int totalNoOfAuthor =  0;
		int totalNoOfCitation = 0;
		int lowestYear = 9999;
		int highestYear = 0;
		HashSet<SerializedJournal> journalSet = new HashSet<>();
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder xmlBuilder = builderFactory.newDocumentBuilder();
		Document doc = xmlBuilder.newDocument();

		Element rootElement = doc.createElement(conferenceName);
		doc.appendChild(rootElement);

		Element main = doc.createElement("main");
		Element citationslists = doc.createElement("citationlists");
		Element authorElement = doc.createElement("NoOfAuthor");
		Element noOfcitationElement = doc.createElement("noOfCitation");
		Element yearRangeElement = doc.createElement("yearRange");
		rootElement.appendChild(authorElement);
		rootElement.appendChild(noOfcitationElement);
		rootElement.appendChild(yearRangeElement);
		rootElement.appendChild(main);
		rootElement.appendChild(citationslists);

		for (SerializedJournal journal : journalList) {
			if (!journalSet.contains(journal)) {
				Element journalElement = doc.createElement("journal");
				appendChildToELement("title", journal.title, journalElement, doc);
				appendChildToELement("authir", journal.author, journalElement, doc);
				appendChildToELement("affiliation", journal.affiliation, journalElement, doc);
				appendChildToELement("abstractText", journal.abstractText, journalElement, doc);
				main.appendChild(journalElement);
				journalSet.add(journal);
			}
			totalNoOfCitation+= journal.citations.size();

			for (SerializedCitation citation : journal.citations) {
				Element citationElement = doc.createElement("citation");
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
