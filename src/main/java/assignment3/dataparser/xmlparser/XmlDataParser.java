package assignment3.dataparser.xmlparser;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.parsers.SAXParserFactory;

import assignment3.dataparser.DataParser;

public class XmlDataParser implements DataParser {

    public void parseFile(String filename) throws FileNotFoundException{
        requireNonNull(filename);

        File file = new File(filename);

        if (file.exists() && file.isFile()) {
            throw new FileNotFoundException("File not found : " + file.getAbsolutePath());
        }

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

    }
}
