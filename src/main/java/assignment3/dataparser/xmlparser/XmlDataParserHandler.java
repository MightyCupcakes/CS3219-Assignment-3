package assignment3.dataparser.xmlparser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import assignment3.dataparser.SerializedCitation;
import assignment3.dataparser.SerializedJournal;

public class XmlDataParserHandler extends DefaultHandler {

    private static final BiConsumer<SerializedJournal.Builder, String> NOOP = (b, s) -> {};

    private static Map<String, BiConsumer<SerializedJournal.Builder, String>> elementHandler;

    private SerializedJournal.Builder builder;
    private List<SerializedCitation> citationList;
    private BiConsumer<SerializedJournal.Builder, String> handler;

    private SerializedJournal journal;

    static {
        elementHandler = new HashMap<>();

        elementHandler.put("title", (b, s) -> b.withTitle(s));
        elementHandler.put("author", (b, s) -> b.withAuthor(s));
        elementHandler.put("affiliation", (b, s) -> b.withAffiliation(s));
        elementHandler.put("abstract", (b, s) -> b.withAbstract(s));
    }

    public SerializedJournal getJournal() {
        return journal;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase("algorithm")
                && attributes.getValue("name").equalsIgnoreCase("ParsHed")) {

            builder = new SerializedJournal.Builder();
        } else if (qName.equalsIgnoreCase("citationList")) {
            citationList = new LinkedList<>();
        } else if (builder != null && elementHandler.containsKey(qName.toLowerCase())) {
            handler = elementHandler.get(qName.toLowerCase());
        } else {
            handler = NOOP;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (citationList != null && qName.equalsIgnoreCase("algorithm")) {
            journal = builder.build();
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        handler.accept(builder, new String(ch, start, length));
    }
}
