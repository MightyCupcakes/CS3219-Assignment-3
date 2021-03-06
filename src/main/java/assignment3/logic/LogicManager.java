package assignment3.logic;

import static java.util.Objects.isNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.google.common.base.Strings;

import assignment3.dataparser.DataParser;
import assignment3.dataparser.exceptions.StopParserException;
import assignment3.dataparser.jsonparser.JsonDataParser;
import assignment3.dataparser.jsonparser.JsonIdDataParser;
import assignment3.dataparser.xmlparser.XmlDataParser;
import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;
import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.model.Model;
import assignment3.model.ModelManager;
import assignment3.schema.SchemaBase;


public class LogicManager implements Logic{

    private XmlDataParser parser;
    private Model model;

    public LogicManager () {
        parser = new XmlDataParser();
        model = new ModelManager();
        QueryBuilder.setLogicTo(this);
    }

    @Override
    public void parseAndSaveRawJSONData(String file) throws Exception {
        JsonIdDataParser idDataParser = new JsonIdDataParser();

        convertJsonToJournal(idDataParser, file);
        idDataParser.linkCitationsIdToJournal();

        model.saveJournalData(idDataParser.getJournals(), "A4");
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
    @SuppressWarnings("unchecked")
    public List<SerializedJournalCitation> getDataFromTableWithNoCitations(String tableName,
        Collection<SchemaBase> schemas) throws Exception {

        Map<Integer, SerializedJournal> journalMap = model.getJournal(tableName);
        List<SerializedJournalCitation> journalCitationList = new ArrayList<>();

        for (SerializedJournal journal : journalMap.values()) {

            for (SchemaBase schema : schemas) {
                Collection<String> attributes = (Collection<String>) schema.getSplittingFunction().apply(journal);

                for (String attribute : attributes) {
                    SerializedJournal duplicateJournal = (SerializedJournal) schema.getDuplicateGenerator().apply(journal, attribute);
                    journalCitationList.add(new SerializedJournalCitation(duplicateJournal, null));
                }
            }
        }
        return journalCitationList;
    }

    @Override
    public List<SerializedJournalCitation> getDataFromTableWithCitations(String tableName) throws Exception {

        Map<Integer, SerializedJournal> journalMap = model.getJournal(tableName);
        Map<Integer, List<SerializedCitation>> citationMap = model.getCitations(tableName);
        List<SerializedJournalCitation> journalCitationLists = new ArrayList<>();

        journalMap.forEach( (id, journal) -> {
            List<SerializedCitation> citationList = citationMap.get(id);

            if (isNull(citationList)) {
                journalCitationLists.add(new SerializedJournalCitation(journal, null));
            } else {
                for (SerializedCitation citation : citationList) {
                    journalCitationLists.add(new SerializedJournalCitation(journal, citation));
                }
            }
        });
        return journalCitationLists;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SerializedJournalCitation> getDataFromTableWithCitations(String tableName,
        Collection<SchemaBase> schemas) throws Exception {

        Map<Integer, SerializedJournal> journalMap = model.getJournal(tableName);
        Map<Integer, List<SerializedCitation>> citationMap = model.getCitations(tableName);
        List<SerializedJournalCitation> journalCitationLists = new ArrayList<>();


        journalMap.forEach( (id, journal) -> {
            List<SerializedCitation> citationList = citationMap.get(id);

            if (isNull(citationList)) return;

            for (SchemaBase schema : schemas) {
                Collection<String> attributes = (Collection<String>) schema.getSplittingFunction().apply(journal);

                for (String attribute : attributes) {
                    SerializedJournal duplicateJournal = (SerializedJournal) schema.getDuplicateGenerator().apply(journal, attribute);

                    for (SerializedCitation citation : citationList) {
                        journalCitationLists.add(new SerializedJournalCitation(duplicateJournal, citation));
                    }
                }
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
                parser.parseData(conference);
                journalList.add(parser.getJournal());
            } catch (StopParserException e) {
                Logger.getLogger(this.getClass().toString())
                        .warning("File has no journal, skipping file: " + conference);
            }
        }
        return journalList;
    }

    private List<SerializedJournal> convertJsonToJournal(JsonIdDataParser idDataParser, String fileName) throws Exception {
        File file = new File(fileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<SerializedJournal> journals = new ArrayList<>();

        for (int i = 0; i < 200000; i ++) {
            DataParser parser = new JsonDataParser();
            String line = reader.readLine();

            if (isNull(line)) {
                break;
            }

            parser.parseData(line);

            SerializedJournal journal = parser.getJournal();
            idDataParser.addJournal(journal);
            journals.add(journal);
        }

        return journals;
    }

    @Override
    public void saveResultIntoCsv(String jsonStringData, String filename) throws Exception {
        JsonReader jsonReader = Json.createReader(new StringReader(jsonStringData));
        JsonArray jsonArr = jsonReader.readArray();
        List<List<String>> dataList = new ArrayList<>();
        dataList.add(getColumnHeaderList(jsonArr));
        for (int i = 0; i < jsonArr.size(); i++) {
            JsonObject obj = jsonArr.getJsonObject(i);
            Iterator<String> keyIterator = obj.keySet().iterator();
            List<String> valueList = new ArrayList<>();
            while (keyIterator.hasNext()) {
                String key = keyIterator.next();
                valueList.add(obj.getString(key));
            }
            dataList.add(valueList);
        }

      //  LocalDateTime datetime = LocalDateTime.now();
       // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        model.writeResultIntoCsvFile(filename, dataList);
    }

    private List<String> getColumnHeaderList(JsonArray json) throws Exception {
        List<String> headerList = new ArrayList<>();
        JsonObject obj = json.getJsonObject(0);
        Iterator<String> keyIterator = obj.keySet().iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            headerList.add(key);
        }

//        switch(taskType) {
//        case 1:
//            headerList = Arrays.asList("author", "count");
//            break;
//        case 2:
//            headerList = Arrays.asList("title", "numOfInCitation");
//            break;
//        case 3:
//            headerList = Arrays.asList("yearOfPublication", "count");
//            break;
//        case 4:
//        	headerList = Arrays.asList("journalId", "journalTitle", "journalAuthors", "citedJournalId", "citedJournalTitle", "citedJournalAuthors");
//            break;
//        case 5:
//            headerList = Arrays.asList("year", "count");
//            break;
//        default: throw new Exception("Invalid Task Type");
//        }
        return headerList;

    }
}
