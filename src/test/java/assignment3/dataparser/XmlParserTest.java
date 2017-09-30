package assignment3.dataparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

import assignment3.dataparser.xmlparser.XmlDataParser;
import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;

public class XmlParserTest {

    private static final String TEST_DATA = "src/test/data/xmlTestData.xml";

    @Test
    public void testXmlParser() throws Exception {
        XmlDataParser parser = new XmlDataParser();
        parser.parseFile(TEST_DATA);

        SerializedJournal journal = parser.getJournal();

        assertEquals(journal.title, "Wiki-ly Supervised Part-of-Speech Tagging");
        assertEquals(journal.author, "Li V Ben Taskar");
        assertEquals(journal.affiliation, "Information Science INESC-ID Computer");
        assertEquals(journal.abstractText, "Despite significant recent work, purely unsupervised techniques...");

        List<SerializedCitation> expectedCitations = ImmutableList.<SerializedCitation>builder()
                .add(new SerializedCitation.Builder()
                        .withAuthor("A Abeill")
                        .withTitle("Treebanks: Building and Using Parsed Corpora.")
                        .withYear("2003").build())
                .add(new SerializedCitation.Builder()
                        .withTitle("Painless unsupervised learning with features.")
                        .withYear("2010")
                        .withAuthor("Taylor Berg-Kirkpatrick").withAuthor("Alexandre Bouchard")
                        .withAuthor("John DeNero").withAuthor("Dan Klein")
                        .withBooktitle("In Proc. NAACL,").build()
                ).build();

        assertTrue(expectedCitations.equals(journal.citations));
    }
}
