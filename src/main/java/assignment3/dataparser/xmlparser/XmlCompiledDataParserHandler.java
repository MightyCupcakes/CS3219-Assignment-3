package assignment3.dataparser.xmlparser;

import static java.util.Objects.isNull;

import java.util.HashMap;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import assignment3.dataparser.xmlparser.elementparser.CitationElementParser;
import assignment3.dataparser.xmlparser.elementparser.ElementParser;
import assignment3.dataparser.xmlparser.elementparser.JournalElementParser;
import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;

public class XmlCompiledDataParserHandler extends DefaultHandler{


    private ElementParser currentElementParser;
    private JournalElementParser journalElementParser;
    private String conferenceName;
    private int counterId;
    private HashMap<Integer, SerializedJournal> journalMap;
    private HashMap<Integer, List<SerializedCitation>> citationMap;
    
    public XmlCompiledDataParserHandler(String conferenceName) {
		this.conferenceName = conferenceName;
		journalMap = new HashMap<>();
		citationMap = new HashMap<>();
		counterId = 1;
	}

	public HashMap<Integer, SerializedJournal> getJournalMap() {
        return journalMap;
    }
	public HashMap<Integer, List<SerializedCitation>> getCitationMap() {
		return citationMap;
	}

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
    	if (qName.equalsIgnoreCase("main")) {
    		currentElementParser = new JournalElementParser();
    		return;
    	}
    	
    	if (qName.equalsIgnoreCase("citationlists")) {
        	this.counterId = 1;
            journalElementParser = (JournalElementParser) currentElementParser;
            currentElementParser = new CitationElementParser();
            currentElementParser.openElement(qName);
            return;
    	}
    		
    	String attrValue = attributes.getValue("id");

    	if (qName.equalsIgnoreCase("journal") && !Integer.toString(counterId).equalsIgnoreCase(attrValue)) {
			addNewJournal(counterId, false);
			counterId = Integer.parseInt(attrValue);
			currentElementParser.openElement(qName);
			return;
    	}
    	
    	if (qName.equalsIgnoreCase("citation") && !Integer.toString(counterId).equalsIgnoreCase(attrValue)) {
			addCitationToJournal(counterId, false);
            currentElementParser = new CitationElementParser();
            counterId = Integer.parseInt(attrValue);
			currentElementParser.openElement(qName);
			return;
    	}
 
    	
        if (currentElementParser != null) {
            currentElementParser.openElement(qName);
        }

    }

	@Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
    	if (qName.equalsIgnoreCase(this.conferenceName)) {
            assert currentElementParser instanceof CitationElementParser;
            addNewJournal(counterId, true);
            addCitationToJournal(counterId, true);        
        } else if (currentElementParser != null) {
            currentElementParser.closeElement(qName);
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if (currentElementParser != null) {
            currentElementParser.parse(new String(ch, start, length));
        }
    }
    
    private void addNewJournal(int key, boolean isEndElement) {
		if (isEndElement == true) {
			SerializedJournal journal = journalElementParser.getJournal();
			journalMap.put(key, journal);
		} else {
	    	journalElementParser = JournalElementParser.class.cast(currentElementParser);
	    	SerializedJournal journal = journalElementParser.getJournal();
	    	journalMap.put(key, journal);
            journalElementParser.setNewJournal();
		}
    }
    private void addCitationToJournal(int key, boolean isEndElement) {
    	CitationElementParser citationElementParser = CitationElementParser.class.cast(currentElementParser);
		List<SerializedCitation> citationList = citationElementParser.getCitations();
		citationMap.put(key, citationList);
	}
}
