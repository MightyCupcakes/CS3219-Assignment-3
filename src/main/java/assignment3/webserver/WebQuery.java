package assignment3.webserver;

import java.util.Map;

public interface WebQuery {


	void generateTopNXofYGraph(Map<String, String> data) ;
	void generateTrendGraph(Map<String, String> data) ;
	void generateContemporaryGraph(Map<String, String> data);
	void generateNewGraph(Map<String, String> data);
	String retrieveDataForDropDown(String attribute);
}
