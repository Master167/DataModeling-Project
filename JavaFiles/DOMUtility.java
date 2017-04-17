/*
 * DOMUtility is a utility class used to facilitate DOM 
 * operations which are repetitively used throughout various 
 * file manipulation operations the program.  The DOMUtility
 *  contains the attributes DocumentBuilderFactory, DocumentBuilder, 
 *  and a Document attributes which are required objects utilized 
 *  build DOM Document objects.  The DOMUtility contains the methods 
 *  XMLtoDOM and createDOM.
 * 
 */

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DOMUtility {

	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;
	private Document doc;
	
	public DOMUtility() {
		
		dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 *  Returns Document object with xml file already parsed into it
	 */
	public Document XMLtoDOM(File xmlFile) {
		
		doc = dBuilder.newDocument();
		try {
			doc = dBuilder.parse(xmlFile);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		} 
		return doc;
	}// end XMLtoDOM()
	
	/* 
	 * Returns empty DOM Document object
	 */
	public Document createDOM() {
		
		doc = dBuilder.newDocument();
		return doc;
	}// end createDOM()
	
}
