package assignment3.dataparser.xmlparser.elementparser;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import assignment3.datarepresentation.SerializedCitation;

public class CitationElementParser implements ElementParser {

    private static Map<String, BiConsumer<SerializedCitation.Builder, String>> elementHandler;

    private Deque<String> currentElement;
    private List<SerializedCitation> citations;
    private SerializedCitation.Builder builder;
    
    static {
        elementHandler = new HashMap<>();

        elementHandler.put("author", (b, s) -> b.withAuthor(s));
        elementHandler.put("title", (b, s) -> b.withTitle(s));
        elementHandler.put("date", (b, s) -> b.withYear(s));
        elementHandler.put("booktitle", (b, s) -> b.withBooktitle(s));
    }

    public CitationElementParser() {
        currentElement = new LinkedList<>();
        citations = new LinkedList<>();
        builder = new SerializedCitation.Builder();
    }

    @Override
    public void openElement(String element) {
        currentElement.push(element.toLowerCase());
    }

    @Override
    public void closeElement(String element) {
    	if (currentElement.peek() == null) {
    		return;
    	}
        String closed = currentElement.pop();
        if (closed.equalsIgnoreCase("citation")) {
            citations.add(builder.build());
            builder = new SerializedCitation.Builder();
        }
    }

    @Override
    public void parse(String text) {
        if (elementHandler.containsKey(currentElement.peek())) {
            elementHandler.get(currentElement.peek()).accept(builder, text);
        }
    }

    public List<SerializedCitation> getCitations() {
        return citations;
    }

}
