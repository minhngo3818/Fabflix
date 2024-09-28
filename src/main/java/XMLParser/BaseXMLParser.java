package XMLParser;

import org.xml.sax.helpers.DefaultHandler;
import java.io.File;
import java.net.URL;

public class BaseXMLParser extends DefaultHandler {
    protected String tempVal;
    protected int recordCount = 0;
    protected File resourceFile;

    protected void loadFile(String filePath) {
        URL resourceURL = getClass().getClassLoader().getResource(filePath);
        if (resourceURL != null) {
            resourceFile = new File(resourceURL.getFile());
        } else {
            System.out.println("File not found");
            resourceFile = null; // or you can throw an exception here if necessary
        }
    }

    protected void printData() {}
}
