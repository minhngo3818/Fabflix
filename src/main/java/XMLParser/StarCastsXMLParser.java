package XMLParser;

import XMLParser.data.CastInfo;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class StarCastsXMLParser extends BaseXMLParser {
    private Set<CastInfo> castInfoSet;
    private CastInfo tempCast;
    private int parsedCount = 0;
    private Set<CastInfo> invalidCasts;

    public StarCastsXMLParser() {
        // Load file to resourceFile
        loadFile("data/casts124.xml");

        // Init lists
        castInfoSet = new HashSet<>();
        invalidCasts = new HashSet<>();
    }

    public void printReport(boolean isFullBody) {
        System.out.println(getClass().getSimpleName() + ": Report parsing process");
        if (isFullBody) {
            for (CastInfo cast: castInfoSet) {
                System.out.println("- " + cast.toString());
            }
        }
        System.out.println("- " + parsedCount + " parsed casts");
        System.out.println("- " + invalidCasts.size() + " invalid casts");
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
        if (qName.equalsIgnoreCase("m")) {
            tempCast = new CastInfo();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("m")) {
            if (castInfoSet.contains(tempCast) || tempCast.getStarName().equals("s a")) {
                invalidCasts.add(tempCast);
            } else {
                castInfoSet.add(tempCast);
            }
            parsedCount += 1;
        }  else if (qName.equalsIgnoreCase("a")) {
            tempCast.setStarName(tempVal.replaceAll("~", " ").trim());
        } else if (qName.equalsIgnoreCase("f")) {
            tempCast.setMovieId(tempVal.trim());
        }
    }

    public Set<CastInfo> getCastsInfoSet() {
        return castInfoSet;
    }

    public Set<CastInfo> getInvalidCasts() { return invalidCasts;}

    public static void main(String[] args) {
        StarCastsXMLParser scxp = new StarCastsXMLParser();
        scxp.runParser();
        scxp.printReport(false);
    }
}
