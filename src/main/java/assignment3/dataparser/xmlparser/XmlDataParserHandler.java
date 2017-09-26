package assignment3.dataparser.xmlparser;

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

    public SerializedJournal getJournal() {
        return journal;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase("algorithm")
                && attributes.getValue("name").equalsIgnoreCase("ParsHed")) {

            currentElementParser = new JournalElementParser();
        } else if (qName.equalsIgnoreCase("citationList")) {
            journalElementParser = (JournalElementParser) currentElementParser;
            currentElementParser = new CitationElementParser();
            currentElementParser.openElement("citationList");
        } else {
            if (currentElementParser != null) {
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
}
