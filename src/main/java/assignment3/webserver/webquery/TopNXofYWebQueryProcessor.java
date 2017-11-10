package assignment3.webserver.webquery;

import assignment3.webserver.WebServerManager;
import assignment3.webserver.registry.RegisterProcessor;
import assignment3.webserver.webrequest.WebRequest;

@RegisterProcessor( requestType = "topnxofy")
public class TopNXofYWebQueryProcessor implements WebQueryProcessor{

	@Override
	public boolean processAndSaveIntoCSV(WebServerManager manager, WebRequest webRequest) {
		// TODO Auto-generated method stub
		return false;
	}

}
