package assignment3.dataparser.xmlparser;

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

        String attrValue = attributes.getValue("id");

        if (qName.equalsIgnoreCase("main")) {
            // Creates a new journal parser to parse the journals following this tag
            currentElementParser = new JournalElementParser();

        } else if (qName.equalsIgnoreCase("journal")) {
            // Starting tag of a new journal, update counter id to its ID
            counterId = Integer.parseInt(attrValue);
            currentElementParser.openElement(qName);

        } else if (qName.equalsIgnoreCase("citationlists")) {
            // End of journal parsing, reset counter id to 0 (so that the first citation
            // will automatically update the counter id to the journal id it is linked to
            this.counterId = 0;

            journalElementParser = (JournalElementParser) currentElementParser;
            currentElementParser = new CitationElementParser();
            currentElementParser.openElement(qName);

        } else if (qName.equalsIgnoreCase("citation") && !Integer.toString(counterId).equals(attrValue)) {

            if (counterId > 0) {
                // Only link citations if the counter id is non-zero (because we set it to 0 just now)
                // and if it is 0, no citations has been parsed yet.
                addCitationToJournal(counterId, false);
            }

            currentElementParser = new CitationElementParser();
            // Update counter id to the linked journal id.
            counterId = Integer.parseInt(attrValue);
            currentElementParser.openElement(qName);

        } else if (currentElementParser != null) {
            currentElementParser.openElement(qName);
        }

    }

	@Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("citationlists")) {
            assert currentElementParser instanceof CitationElementParser;
            // Links the last citation to its journal
            addCitationToJournal(counterId, true);

        } else if (qName.equalsIgnoreCase("journal")) {
            addNewJournal(counterId);
            currentElementParser.closeElement(qName);

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
    
    private void addNewJournal(int key) {
        journalElementParser = JournalElementParser.class.cast(currentElementParser);
        SerializedJournal journal = journalElementParser.getJournal();
        journalMap.put(key, journal);
        journalElementParser.setNewJournal();

    }
    private void addCitationToJournal(int key, boolean isEndElement) {
    	CitationElementParser citationElementParser = CitationElementParser.class.cast(currentElementParser);
		List<SerializedCitation> citationList = citationElementParser.getCitations();
		citationMap.put(key, citationList);
	}
}
