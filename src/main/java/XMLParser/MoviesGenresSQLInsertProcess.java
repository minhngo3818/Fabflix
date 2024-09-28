package XMLParser;

import XMLParser.data.GenresMoviesInfo;
import XMLParser.data.MovieInfo;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MoviesGenresSQLInsertProcess {
    private final MoviesGenresXMLParser mgxp;
    private int insertedMoviesCount = 0;
    private int insertedGenresCount = 0;
    private String movieIdPrefix = "";  // Prefix of movie id in database
    private int movieIdIntSuffix = 0;   // Numeric part of movie id in database
    private final Map<String, String> movieOldNewIdMap;   // Map id from xml to new id in database
    private int insertedGenresInMovieCount = 0;

    public MoviesGenresSQLInsertProcess() {
        movieOldNewIdMap = new HashMap<>();
        this.mgxp = new MoviesGenresXMLParser();
    }


    public void runProcess(Connection connection) throws SQLException {
        runParser();
        runInsertLoneTablesProcess(connection);
        runInsertRelatedTablesProcess(connection);
    }

    public void runParser() {
        // Run StarActorsXMLParser
        mgxp.runParser();
    }

    public void runInsertLoneTablesProcess(Connection connection) throws SQLException {
        insertGenres(connection);
        insertMovies(connection);
    }

    public void runInsertRelatedTablesProcess(Connection connection) throws SQLException {
        insertGenresInMovies(connection);
    }

    private void insertMovies(Connection connection) throws  SQLException {
        System.out.println(getClass().getSimpleName() + ": Inserting into movies ...");
        int[] iNoRow = null;
        String insertQuery = "INSERT INTO movies (id, title, director, year) VALUES(?,?,?,?)";
        PreparedStatement stmt = null;
        stmt = connection.prepareStatement(insertQuery);

        for (MovieInfo entry : mgxp.getMovieList().values()) {
//            System.out.println(getClass().getSimpleName() + ": Insert " + entry.toString());
            String newMovieId = generateMovieId();
            movieOldNewIdMap.put(entry.getId(), newMovieId);

            stmt.setString(1, newMovieId);
            stmt.setString(2, entry.getTitle());
            stmt.setString(3, entry.getDirector());
            stmt.setInt(4, entry.getYear());
            stmt.addBatch();
        }
        iNoRow = stmt.executeBatch();

        insertedMoviesCount = iNoRow.length;
        System.out.println(getClass().getSimpleName() + ": Insert into movies completed!");
    }

    private void insertGenres(Connection connection) throws SQLException {
        System.out.println(getClass().getSimpleName() + ": Inserting into genres ...");
        int[] iNoRow = null;
        String insertQuery = "INSERT INTO genres (name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = ?)";
        PreparedStatement stmt = connection.prepareStatement(insertQuery);

        for (String genre : mgxp.getGenreSet()) {
//            System.out.println(getClass().getSimpleName() + ": Insert " + genre);
            stmt.setString(1, genre);
            stmt.setString(2, genre);
            stmt.addBatch();
        }

        iNoRow = stmt.executeBatch();
        insertedGenresCount = iNoRow.length;
        System.out.println(getClass().getSimpleName() + ": Insert into genres completed! ");
    }

    private void insertGenresInMovies(Connection connection) throws SQLException {
        System.out.println(getClass().getSimpleName() + ": Inserting into genres_in_movies ...");
        int[] iNoRow = null;
        String insertQuery = "INSERT INTO genres_in_movies (genreId, movieId) VALUES ( " +
                "    (SELECT id FROM genres WHERE name = ? ), " +
                "    ? ) ";
        PreparedStatement stmt = connection.prepareStatement(insertQuery);

        for (GenresMoviesInfo entry : mgxp.getGenresMoviesList()) {
//            System.out.println(getClass().getSimpleName() + ": Insert " + entry.toString());

            String newMovieId = movieOldNewIdMap.get(entry.getMovieId());

            stmt.setString(1, entry.getName());
            stmt.setString(2, newMovieId);
            stmt.addBatch();
        }

        iNoRow = stmt.executeBatch();
        insertedGenresInMovieCount = iNoRow.length;
        System.out.println(getClass().getSimpleName() + ": Insert into genres_in_movies completed!");
    }

    public void initStartMovieId(Connection connection) throws SQLException {
        // Initialize movie id before inserting
        // Run this function outside the transaction scope

        try {
            String lastId = "";

            String query = "SELECT max(id) as lastId FROM movies";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                lastId = rs.getString("lastId");
            }

            movieIdPrefix = lastId.substring(0,2);
            movieIdIntSuffix = Integer.parseInt(lastId.substring(2));
        } catch( Exception e) {
            e.printStackTrace();
        }
    }

    public String generateMovieId() {
        movieIdIntSuffix += 1;
        return movieIdPrefix + String.format("%07d", movieIdIntSuffix);
    }

    public int getInsertedMoviesCount() {
        return insertedMoviesCount;
    }

    public int getInsertedGenresCount() {
        return insertedGenresCount;
    }

    public int getInsertedGenresInMovieCount() {
        return insertedGenresInMovieCount;
    }

    public Map<String, String> getMovieOldNewIdMap() {
        return movieOldNewIdMap;
    }

    public void printReport() {
        mgxp.printReport(false);
        System.out.println(getClass().getSimpleName() + ": Report insert process");
        System.out.println("- " + insertedGenresCount + " inserted genres");
        System.out.println("- " + insertedMoviesCount + " inserted movies");
        System.out.println("- " + insertedGenresInMovieCount + " inserted genres_in_movies records");
    }
}
