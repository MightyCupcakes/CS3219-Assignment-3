package assignment3.dataparser;

import java.io.FileNotFoundException;

import org.xml.sax.SAXException;

public interface DataParser {
    void parseFile(String filename) throws FileNotFoundException, SAXException;
}
