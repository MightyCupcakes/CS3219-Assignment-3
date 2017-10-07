package assignment3.logic;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import assignment3.dataparser.xmlparser.XmlDataParser;
import assignment3.dataparser.xmlparser.XmlDataParserHandler;
import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;
import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.model.Model;
import assignment3.model.ModelManager;


public class LogicManager implements Logic{

    private XmlDataParser parser;
    private Model model;

	public LogicManager () {
		parser = new XmlDataParser();
		model = new ModelManager();
		QueryBuilder.setLogicTo(this);
	}

	@Override
	public void parseAndSaveRawData(String folder) throws Exception {
		File conferenceFolder = new File(folder);
		String conferenceName  = conferenceFolder.getName();
		List<String> conferenceLists = getListOfConferences(folder);
		List<SerializedJournal> journalList = convertToJournals(conferenceLists);

		model.saveJournalData(journalList, conferenceName);
	}

	@Override
	public List<SerializedJournalCitation> getDataFromTableWithNoCitations(String tableName) throws Exception {
		Map<Integer, SerializedJournal> journalMap = model.getJournal(tableName);
		return journalMap.values().stream().map(journal -> {
			return new SerializedJournalCitation(journal, null);
		}).collect(Collectors.toList());
	}

	@Override
	public List<SerializedJournalCitation> getDataFromTableWithCitations(String tableName) throws Exception {
		Map<Integer, SerializedJournal> journalMap = model.getJournal(tableName);
		Map<Integer, List<SerializedCitation>> citationMap = model.getCitations(tableName);
		List<SerializedJournalCitation> journalCitationLists = new ArrayList<>();
		
		journalMap.forEach( (id, journal) -> {
			List<SerializedCitation> citationList = citationMap.get(id);
			for (SerializedCitation citation : citationList) {
				journalCitationLists.add(new SerializedJournalCitation(journal, citation));
			}
		});
		return journalCitationLists;
	}

	private List<String> getListOfConferences(String folder) throws Exception {
		return Files.walk(Paths.get(folder))
				.filter(Files::isRegularFile)
				.map(Path::toString)
				.collect(Collectors.toList());
	}
	private List<SerializedJournal> convertToJournals(List<String> conferenceLists) throws Exception {
		List<SerializedJournal> journalList = new ArrayList<>();

		for(String conference : conferenceLists) {
		    try {
                parser.parseFile(conference);
                journalList.add(parser.getJournal());
            } catch (XmlDataParserHandler.MySAXTerminatorException e) {
                Logger.getLogger(this.getClass().toString())
                        .warning("File has no journal, skipping file: " + conference);
            }
		}
		return journalList;
	}
}
