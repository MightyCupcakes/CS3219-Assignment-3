package assignment3.dataparser.xmlparser;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.parsers.SAXParserFactory;

import assignment3.dataparser.DataParser;
import assignment3.datarepresentation.SerializedJournal;

public class XmlDataParser implements DataParser {

    private SerializedJournal journal;

    public void parseFile(String filename) throws FileNotFoundException{
        requireNonNull(filename);

        File file = new File(filename);

        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("File not found : " + file.getAbsolutePath());
        }

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        XmlDataParserHandler handler = new XmlDataParserHandler();

        try {
            saxParserFactory.newSAXParser().parse(file, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        journal = handler.getJournal();
    }
    
    public void parseCompiledFile(String filename, String conferenceName) throws FileNotFoundException{
        requireNonNull(filename);

        File file = new File(filename);

        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("File not found : " + file.getAbsolutePath());
        }

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        XmlCompiledDataParserHandler  handler = new XmlCompiledDataParserHandler (conferenceName);

        try {
            saxParserFactory.newSAXParser().parse(file, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SerializedJournal getJournal() {
        return journal;
    }
}
