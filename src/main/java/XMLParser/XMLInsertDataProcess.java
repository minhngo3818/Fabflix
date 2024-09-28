package XMLParser;

import data.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;

public class XMLInsertDataProcess {
    public static void main (String[] args) throws SQLException, InterruptedException {
        long startTime = System.currentTimeMillis();

        MoviesGenresSQLInsertProcess movieInsertProcess = new MoviesGenresSQLInsertProcess();
        StarsMoviesSQLInsertProcess starsMoviesInsertProcess = new StarsMoviesSQLInsertProcess();

        // Run parsers
        movieInsertProcess.runParser();
        starsMoviesInsertProcess.runParser();

        // Insert new records to tables
        DatabaseManager dbManager = new DatabaseManager();
        Connection connection = dbManager.getConnection();

        System.out.println(connection.toString());

        movieInsertProcess.initStartMovieId(connection);

        try {
            connection.setAutoCommit(false);

            movieInsertProcess.runInsertLoneTablesProcess(connection);
            movieInsertProcess.runInsertRelatedTablesProcess(connection);
            starsMoviesInsertProcess.runInsertLoneTablesProcessSQL(connection);
            starsMoviesInsertProcess.runInsertRelatedTablesProcess(connection, movieInsertProcess.getMovieOldNewIdMap());

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        }

        long ms = (System.currentTimeMillis()-startTime);
        long remainderMS = ms % 1000;
        long sec = (ms/1000) % 60;
        movieInsertProcess.printReport();
        starsMoviesInsertProcess.printReport();
        System.out.println("Runtime: " +  sec + " sec " + remainderMS + " ms");
    }
}
