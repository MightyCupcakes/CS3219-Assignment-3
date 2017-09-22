package assignment3.dataparser.xmlparser.elementparser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import assignment3.dataparser.SerializedCitation;
import assignment3.dataparser.SerializedJournal;

public class JournalElementParser implements ElementParser {

    private static Map<String, BiConsumer<SerializedJournal.Builder, String>> elementHandler;

    private String currentElement;
    private SerializedJournal.Builder builder;

    static {
        elementHandler = new HashMap<>();

        elementHandler.put("title", (b, s) -> b.withTitle(s));
        elementHandler.put("author", (b, s) -> b.withAuthor(s));
        elementHandler.put("affiliation", (b, s) -> b.withAffiliation(s));
        elementHandler.put("abstract", (b, s) -> b.withAbstract(s));
    }

    public JournalElementParser() {
        currentElement = "";
        builder = new SerializedJournal.Builder();
    }

    @Override
    public void openElement(String element) {
        currentElement = element.toLowerCase();
    }

    @Override
    public void closeElement(String element) {
        currentElement = "";
    }

    @Override
    public void parse(String text) {
        if (elementHandler.containsKey(currentElement)) {
            elementHandler.get(currentElement).accept(builder, text);
        }
    }

    public void setCitations(List<SerializedCitation> citationList) {
        builder.withCitations(citationList);
    }

    public SerializedJournal getJournal() {
        return builder.build();
    }
}
