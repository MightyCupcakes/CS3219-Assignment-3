package assignment3.dataparser;

import java.io.FileNotFoundException;

import org.xml.sax.SAXException;

import assignment3.dataparser.exceptions.StopParserException;
import assignment3.datarepresentation.SerializedJournal;

public interface DataParser {
    void parseData(String data) throws StopParserException;
    SerializedJournal getJournal();
}
