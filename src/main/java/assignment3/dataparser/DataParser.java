package assignment3.dataparser;

import java.io.FileNotFoundException;

public interface DataParser {
    void parseFile(String filename) throws FileNotFoundException;
}
