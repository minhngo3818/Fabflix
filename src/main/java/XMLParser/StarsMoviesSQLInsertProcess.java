package XMLParser;

import XMLParser.data.CastInfo;
import XMLParser.data.StarInfo;
import java.sql.*;
import java.util.*;

public class StarsMoviesSQLInsertProcess {
    private final StarActorsXMLParser saxp;
    private final StarCastsXMLParser scxp;
    private int starI = 10000000;
    private Map<String, String> starNameIdMap;

    private final Set<String> existedStarIdMovieSet;
    private int insertedStarsCount = 0;

    private int insertedStarsInMoviesCount = 0;

    public StarsMoviesSQLInsertProcess() {
        this.saxp = new StarActorsXMLParser();
        this.scxp = new StarCastsXMLParser();
        starNameIdMap = new HashMap<>();
        existedStarIdMovieSet = new HashSet<>();
    }

    public void runProcess(Connection connection, Map<String, String> movieOldNewIdMap) throws SQLException {
        runParser();
        runInsertLoneTablesProcessSQL(connection);
        runInsertRelatedTablesProcess(connection, movieOldNewIdMap);
    }

    public void runParser() {
        // Run StarActorsXMLParser
        saxp.runParser();

        // Run StarCastsXMLParser
        scxp.runParser();
    }

    public void runInsertLoneTablesProcessSQL(Connection connection) throws SQLException {
        insertStars(connection);
    }

    public void runInsertRelatedTablesProcess(Connection connection, Map<String, String> movieOldNewIdMap) throws SQLException {
        insertStarsInMovies(connection, movieOldNewIdMap);
    }

    private void insertStars(Connection connection) throws SQLException {
        System.out.println(getClass().getSimpleName() + ": Inserting into stars ...");
        int[] iNoRow = null;
        String insertQuery = "INSERT INTO stars (id, name, birthYear) VALUES(?,?,?)";
        PreparedStatement stmt = null;
        stmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

        for (StarInfo entry : saxp.getStarSet()) {
            // System.out.println(getClass().getSimpleName() + ": Insert " + entry.toString());
            String generatedId = generateStarId();
            stmt.setString(1, generatedId);
            stmt.setString(2, entry.getName());
            if (entry.getBirthYear() == null) stmt.setNull(3, Types.INTEGER);
            else stmt.setInt(3, entry.getBirthYear());
            stmt.addBatch();

            starNameIdMap.put(entry.getName(), generatedId);
        }

        iNoRow = stmt.executeBatch();
        insertedStarsCount = iNoRow.length;
        stmt.close();
        System.out.println(getClass().getSimpleName() + ": Insert into stars completed!");
    }

    private void insertStarsInMovies(Connection connection, Map<String, String> movieOldNewIdMap) throws SQLException {
        System.out.println(getClass().getSimpleName() + ": Inserting into stars_in_movies ...");
        int[] iNoRow = null;
        String insertQuery = "INSERT INTO stars_in_movies (starId, movieId) VALUES(?,?) ";
        PreparedStatement stmt = null;
        stmt = connection.prepareStatement(insertQuery);

        for (CastInfo entry : scxp.getCastsInfoSet()) {
            String insertedId = starNameIdMap.get(entry.getStarName());
            String newMovieId = movieOldNewIdMap.get(entry.getMovieId());
            boolean hasMovieId = movieExists(connection,newMovieId);
            boolean hashExistedPairId = existedStarIdMovieSet.contains(insertedId + "-" + newMovieId);
//            System.out.println(getClass().getSimpleName() + ": Insert " + entry.toString() + ", " + newMovieId);
//            System.out.println(getClass().getSimpleName() + ": Insert StarId " + insertedId);

            if (hasMovieId && insertedId != null && !hashExistedPairId) {
                existedStarIdMovieSet.add(insertedId + "-" + newMovieId);
                stmt.setString(1, insertedId);
                stmt.setString(2, newMovieId);
                stmt.addBatch();
            }
        }
        iNoRow = stmt.executeBatch();
        stmt.close();
        insertedStarsInMoviesCount = iNoRow.length;
        System.out.println(getClass().getSimpleName() + ": Insert into stars_in_movies completed!");
    }

    private String generateStarId() {
        // Generate customized starId since it is difficult to retrieve str id after inserting
        String id = "ne" + starI;
        starI += 1;
        return id;
    }

    private boolean movieExists(Connection connection, String movieId) throws SQLException {
        int count = 0;
        String query = "SELECT COUNT(*) FROM movies WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, movieId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            count = rs.getInt(1);
        }

        stmt.close();
        rs.close();
        return count > 0;
    }

    public void printReport() {
        saxp.printReport(false);
        scxp.printReport(false);
        System.out.println(getClass().getSimpleName() + ": Report insert process");
        System.out.println("- " + insertedStarsCount + " inserted stars");
        System.out.println("- " + insertedStarsInMoviesCount + " inserted stars_in_movies");
        int dupStarsInMoviesCount = scxp.getCastsInfoSet().size() - insertedStarsInMoviesCount + scxp.getInvalidCasts().size();
        System.out.println("- " + dupStarsInMoviesCount + " duplicated or invalid stars_in_movies");
    }
}
