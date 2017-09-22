package assignment3.dataparser.xmlparser.elementparser;

public interface ElementParser {
    void openElement(String element);
    void closeElement(String element);
    void parse(String text);
}
