package assignment3.webserver.requestprocessor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import com.google.common.io.Files;

import assignment3.webserver.WebServerConstants;
import assignment3.webserver.WebServerManager;
import assignment3.webserver.WebServerConstants.PremadeQueriesInfo;
import assignment3.webserver.registry.RegisterProcessor;
import assignment3.webserver.webrequest.WebRequest;

@RegisterProcessor( requestType = "retrievePremadeType")
public class PremadeTypeRequestProcessor implements RequestProcessor{
	private final static String HTML_LOCATION = "docs/premadeHtml/";
	private final static String HTML_FILE_TYPE = ".html";
	@Override
	public String handleRequest(WebRequest keyValuePairs) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
		String premadeType = keyValuePairs.getValue("premadeType");
		
		String name = getHtmlFileName(premadeType);
		System.out.println(name);
		File file = new File(HTML_LOCATION + getHtmlFileName(premadeType) + HTML_FILE_TYPE);
		System.out.println(file.exists());
		String response;
		try {
			response = Files.asCharSource(file, WebServerManager.UTF8).read();
		} catch (IOException e) {
			return "";
		}
		
		builder.add("response", response);
		
		
		return builder.build().toString();
	}
	
	private static String getHtmlFileName(String type) {
		
		List<PremadeQueriesInfo> htmlFileNames = WebServerConstants.PREMADE_QUERIES.stream()
			.filter( o -> o.name.equalsIgnoreCase(type)).collect(Collectors.toList());
		
		return htmlFileNames.get(0).htmlFile;
		
	}


}
