package assignment3.dataparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

import assignment3.dataparser.xmlparser.XmlDataParser;
import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;

public class XmlCompiledParserTest {
	private static final String TEST_DATA = "src/test/data/xmlTestDataCompiled.xml";
	
	@Test
	public void testCompiledXmlParser() throws Exception {
        XmlDataParser parser = new XmlDataParser();
        parser.parseCompiledFile(TEST_DATA, "XmlTestDataCompiled");
        
        HashMap<Integer, SerializedJournal> journalMap = parser.getJournalMap();
        HashMap<Integer, List<SerializedCitation>> citationMap = parser.getCitationMap();
        
        SerializedJournal firstJournal = journalMap.get(1);
        SerializedJournal secondJournal = journalMap.get(2);
        
        List<SerializedCitation> firstCitationList = citationMap.get(1);
        List<SerializedCitation> secondCitationList = citationMap.get(2);

        assertEquals(firstJournal.title, "THIS IS A TITLE");
        assertEquals(firstJournal.author, "Yejin Choi");
        assertEquals(firstJournal.affiliation, "Joint Inference for Event Timeline Construction");
        
        assertEquals(secondJournal.title, "Syntactic Transfer Using a Bilingual Lexicon" );
        assertEquals(secondJournal.author, "Adam Pauls Durrett" );
        assertEquals(secondJournal.affiliation, "Computer Science University of California," );
        assertEquals(secondJournal.abstractText, "i love abstract text" );
        
        List<SerializedCitation> expectedCitations_one = ImmutableList.<SerializedCitation>builder()
                .add(new SerializedCitation.Builder()
                        .withTitle("Improved Parsing and POS Tagging Using Inter-Sentence Consistency Constraints")
                        .withYear("2007")
                        .withAuthor("Cool")
                        .withBooktitle("booktitle")
                        .build())
                .add(new SerializedCitation.Builder()
                        .withTitle("(continued) Session 3-PM-2C: NLP Applications Session Chair: Chikara")
                        .withYear("2012")
                        .withAuthor("Saturday")
                        .withBooktitle("booktitle2")
                        .build()
                ).build();
        List<SerializedCitation> expectedCitations_two = ImmutableList.<SerializedCitation>builder()
                .add(new SerializedCitation.Builder()
                        .withAuthor("Erwin Marsi")
                        .withAuthor("Sabine Buchholz")
                        .withTitle("CoNLL-X Shared Task on Multilingual Dependency Parsing.")
                        .withYear("2006")
                        .withBooktitle("In Proceedings of CoNLL,")
                        .build())
                .add(new SerializedCitation.Builder()
                        .withYear("2012")
                        .withAuthor("Wikimedia Foundation")
                        .withBooktitle("booktitle3")
                        .withTitle("a very cool title")
                        .build()
                ).build();
        assertTrue(expectedCitations_one.equals(firstCitationList));
        assertTrue(expectedCitations_two.equals(secondCitationList));
	}
	
}
