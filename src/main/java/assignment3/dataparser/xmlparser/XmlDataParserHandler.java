package assignment3.dataparser.xmlparser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import assignment3.dataparser.SerializedJournal;

public class XmlDataParserHandler extends DefaultHandler {

    private SerializedJournal.Builder builder;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase("algorithm")
                && attributes.getValue("name").equalsIgnoreCase("ParsHed")) {

            builder = new SerializedJournal.Builder();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {

    }
}
