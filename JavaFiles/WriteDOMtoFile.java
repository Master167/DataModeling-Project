import java.io.File;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

class WriteDOMtoFile { 

	private DOMSource source;
	private StringWriter writer;
	private StreamResult result;
	private TransformerFactory factory;
	private Transformer transformer;
	private File outputFile;
	
	WriteDOMtoFile() {
		transformer = null;
	}
	
	public void write(Document doc, String outFileName) {
		outputFile = new File(outFileName);
		source = new DOMSource(doc);
		writer = new StringWriter();
		result = new StreamResult(outputFile);
		factory = TransformerFactory.newInstance();
		try {
			transformer = factory.newTransformer();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "5");
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		String xmlString = writer.toString();
		System.out.println(xmlString);
	}
}