package XMLParser;


import java.io.IOException;
import java.util.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import XMLParser.data.MovieInfo;
import XMLParser.data.GenresMoviesInfo;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class MoviesGenresXMLParser extends BaseXMLParser {

    private final GenreMap genreMap = new GenreMap(); // For translating genre names
    private HashMap<String, MovieInfo> movieList;
    private String tempDirector;
    private MovieInfo tempMovie;
    private Set<String> genreSet;
    private Set<GenresMoviesInfo> genresMoviesSet;
    private int parsedCount = 0;
    private ArrayList<MovieInfo> duplicatedMovies;
    private ArrayList<MovieInfo> inconsistentMovies;


    public MoviesGenresXMLParser()  {
        // Load file to resourceFile
        loadFile("data/mains243.xml");

        // Init lists
        movieList = new HashMap<>();
        genresMoviesSet = new HashSet<>();
        genreSet = new HashSet<>();
        duplicatedMovies = new ArrayList<>();
        inconsistentMovies = new ArrayList<>();
    }

    public void runParser() {
        parseDocument();
    }

    public void printReport(boolean isFullBody) {
        System.out.println(getClass().getSimpleName() + ": Report parsing process");
        if (isFullBody) {
            System.out.println(getClass().getSimpleName() + ": Print out movies");
            for (MovieInfo movie: movieList.values()) {
                System.out.println(getClass().getSimpleName() + ": " + movie.toString());
            }

            System.out.println(getClass().getSimpleName() + ": Print out genres");
            for (String entry : genreSet) {
                System.out.println(getClass().getSimpleName() + ": " + entry);
            }

            System.out.println(getClass().getSimpleName() + ": Print out genres movies");
            for (GenresMoviesInfo entry : genresMoviesSet) {
                System.out.println(getClass().getSimpleName() + ": " + entry.toString());
            }
        }
        System.out.println("- " + parsedCount + " parsed xml movies");
        System.out.println("- " + duplicatedMovies.size() + " duplicated movies");
        System.out.println("- " + inconsistentMovies.size() + " inconsistent movies");
        System.out.println("- " + genreSet.size() + " acquired genres");
        System.out.println("- " + genresMoviesSet.size() + " parsed xml genre-movie pairs");
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
        if (qName.equalsIgnoreCase("film")) {
            tempMovie = new MovieInfo();
            tempMovie.setDirector(tempDirector);
            parsedCount += 1;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("dirname")) {
            if (tempVal.toLowerCase().contains("unknown")) tempDirector = "";
            else tempDirector = tempVal.replace("~", " ");

        } else if (qName.equalsIgnoreCase("film")) {
            if (isDuplicate(tempMovie)) duplicatedMovies.add(tempMovie);
            else if (isInconsistent(tempMovie)) inconsistentMovies.add(tempMovie);
            else {
                // Add movie
                movieList.put(tempMovie.getId(), tempMovie);

                // Add genre movieId pair
                for (String genre : tempMovie.getGenres()) {
                    genresMoviesSet.add(new GenresMoviesInfo(genre, tempMovie.getId()));
                }
            }

        } else if (qName.equalsIgnoreCase("fid")) {
            tempMovie.setId(tempVal.trim());

        } else if (qName.equalsIgnoreCase("t")) {
            tempMovie.setTitle(tempVal);

        } else if (qName.equalsIgnoreCase("cat")) {
            ArrayList<String> tempGenres = genreMap.getTokenizedGenres(tempVal);
            tempMovie.getGenres().addAll(tempGenres);
            genreSet.addAll(tempGenres);

        } else if (qName.equalsIgnoreCase("year")) {
            tempVal = tempVal.replaceAll("[^0-9]", "");
            if (tempVal.length() != 4) tempMovie.setYear(null);
            else tempMovie.setYear(Integer.parseInt(tempVal));
        }
    }

    private boolean isDuplicate(MovieInfo _tempMovie) {
        return movieList.containsValue(_tempMovie) || movieList.containsKey(_tempMovie.getId());
    }

    private boolean isInconsistent(MovieInfo _tempMovie) {
        if (_tempMovie.getId().isEmpty() || _tempMovie.getId().equals("null")) return true;
        if (_tempMovie.getTitle().isEmpty() || _tempMovie.getTitle().equals("NKT")) return true;
        if (_tempMovie.getDirector().isEmpty()) return true;
        if (_tempMovie.getYear() == null) return true;
        if (_tempMovie.getGenres().isEmpty()) return true;
        return false;
    }

    public HashMap<String, MovieInfo> getMovieList() {
        return movieList;
    }

    public Set<String> getGenreSet() {
        return genreSet;
    }

    public Set<GenresMoviesInfo> getGenresMoviesList() {
        return genresMoviesSet;
    }

    public static void main(String[] args) {
        MoviesGenresXMLParser mxp = new MoviesGenresXMLParser();
        mxp.runParser();
        mxp.printReport(false);
    }
}