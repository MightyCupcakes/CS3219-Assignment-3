package assignment3.logic;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import assignment3.dataparser.xmlparser.XmlDataParser;
import assignment3.datarepresentation.SerializedJournal;
import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.logic.Logic;
import assignment3.model.Model;
import assignment3.model.ModelManager;


public class LogicManager implements Logic{

    private XmlDataParser parser;
    private Model model;

	public LogicManager () {
		parser = new XmlDataParser();
		model = new ModelManager();
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
	public List<SerializedJournalCitation> getDataFromTableWithNoCitations(String tableName) {
		return null;
	}

	@Override
	public List<SerializedJournalCitation> getDataFromTableWithCitations(String tableName) {
		return null;
	}

	private List<String> getListOfConferences(String folder) throws Exception {
		return Files.walk(Paths.get(folder))
				.filter(Files::isRegularFile)
				.map(Path::toString)
				.collect(Collectors.toList());
	}
	private List<SerializedJournal> convertToJournals(List<String> conferenceLists) throws Exception{
		List<SerializedJournal> journalList = new ArrayList<>();
		for(String conference : conferenceLists) {
			parser.parseFile(conference);
			journalList.add(parser.getJournal());
		}
		return journalList;
	}
}
