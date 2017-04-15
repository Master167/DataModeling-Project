import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DOMUtility {

	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;
	private Document doc;
	
	public DOMUtility() {
	}
	
	public Document XMLtoDOM(File xmlFile) {// parses XML file and returns DOM object
		
		doc = dBuilder.newDocument();
		try {
			doc = dBuilder.parse(xmlFile);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		} 
		return doc;
	}
	
	public Document createDOM() {// returns empty DOM Document object
		
		doc = dBuilder.newDocument();
		return doc;
	}
	
}
