package XMLParser;

import XMLParser.data.StarInfo;
import data.star.Star;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import utils.ParseNumber;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.*;

public class StarActorsXMLParser extends BaseXMLParser {

    private Set<StarInfo> starSet;
    private StarInfo tempStar;
    private List<StarInfo> duplicateStars;
    private int parsedCount = 0;
    
    public StarActorsXMLParser() {
        // Load file to resourceFile
        loadFile("data/actors63.xml");

        // Init lists
        starSet = new HashSet<>();
        duplicateStars = new ArrayList<>();
    }

    public void printReport(boolean isFullBody) {
        System.out.println(getClass().getSimpleName() + ": Report parsing process");
        if (isFullBody) {
            for (StarInfo star: starSet) {
                System.out.println(getClass().getSimpleName() + ": " + star.toString());
            }

            System.out.println(getClass().getSimpleName() + ": Print duplicated stars");
            for (StarInfo starInfo : duplicateStars) {
                System.out.println(starInfo.toString());
            }
        }
        System.out.println("- " + parsedCount + " parsed stars");
        System.out.println("- " + duplicateStars.size() + " duplicated stars");
    }

    public void runParser() {
        parseDocument();
    }

    private void parseDocument() {

        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse(resourceFile, this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("actor")) {
            tempStar = new StarInfo();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("actor")) {
            if (starSet.contains(tempStar)) duplicateStars.add(tempStar);
            else  starSet.add(tempStar);
            parsedCount += 1;

        } else if (qName.equalsIgnoreCase("stagename")) {
            tempStar.setName(tempVal.trim());

        } else if (qName.equalsIgnoreCase("dob")) {
            tempVal = tempVal.replaceAll("[^0-9]", "");
            if (tempVal.length() != 4) tempStar.setBirthYear(null);
            else tempStar.setBirthYear(Integer.parseInt(tempVal));

        }
    }

    public Set<StarInfo> getStarSet() {
        return starSet;
    }

    public static void main(String[] args) {
        StarActorsXMLParser saxp = new StarActorsXMLParser();
        saxp.runParser();
        saxp.printReport(false);
    }
}
