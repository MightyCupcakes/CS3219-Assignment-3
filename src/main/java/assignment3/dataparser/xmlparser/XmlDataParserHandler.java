package assignment3.dataparser.xmlparser;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import java.util.function.BiConsumer;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import assignment3.datarepresentation.SerializedJournal;
import assignment3.dataparser.xmlparser.elementparser.CitationElementParser;
import assignment3.dataparser.xmlparser.elementparser.ElementParser;
import assignment3.dataparser.xmlparser.elementparser.JournalElementParser;

public class XmlDataParserHandler extends DefaultHandler {

    private BiConsumer<SerializedJournal.Builder, String> handler;

    private SerializedJournal journal;
    private SerializedJournal.Builder builder;
    private ElementParser currentElementParser;
    private JournalElementParser journalElementParser;
    private boolean isVaLidCitation = true;

    public SerializedJournal getJournal() {
        return journal;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("algorithm")
                && attributes.getValue("name").equalsIgnoreCase("ParsHed")) {

            currentElementParser = new JournalElementParser();
        } else if (qName.equalsIgnoreCase("algorithm")
                && attributes.getValue("name").equalsIgnoreCase("ParsCit")) {
            journalElementParser = (JournalElementParser) currentElementParser;

            if (isNull(journalElementParser)) {
                // Stop parsing if the document does not contain any journal info.
                throw new MySAXTerminatorException();
            }

            currentElementParser = new CitationElementParser();
            currentElementParser.openElement(qName);
        } else if(qName.equalsIgnoreCase("citation")
        		&& attributes.getValue("valid").equalsIgnoreCase("false")) {
        	isVaLidCitation = false;
        } else if (qName.equalsIgnoreCase("citation")
        		&& attributes.getValue("valid").equalsIgnoreCase("true")) {
        	isVaLidCitation = true;
            currentElementParser.openElement(qName);
        } else {
            if (currentElementParser != null && isVaLidCitation == true) {
                currentElementParser.openElement(qName);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("algorithms")) {
            assert currentElementParser instanceof CitationElementParser;

            journalElementParser.setCitations(((CitationElementParser) currentElementParser).getCitations());
            journal = journalElementParser.getJournal();
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

    public static class MySAXTerminatorException extends SAXException {

    }
}
