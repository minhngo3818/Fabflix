package services.movies;

import data.movie.MovieSuggestion;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static services.movies.MoviesUtils.formatBooleanKeywords;

public class MovieSuggestionServices {
    public static final int DEFAULT_SUGGESTION_SIZE = 10;

    public List<MovieSuggestion> searchSuggestedMovies(String keywords, DataSource ds) throws Exception {
        ArrayList<MovieSuggestion> suggestionList = new ArrayList<>();
        
        // Reset the previous query
        keywords = formatBooleanKeywords(keywords);

        try (Connection connection = ds.getConnection() ) {

            String query = "SElECT movies.id, movies.title, movies.year FROM movies " +
                    "WHERE MATCH(title) AGAINST ( ? IN BOOLEAN MODE) " +
                    "ORDER BY title LIMIT ? ";


            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, keywords);
            statement.setInt(2, DEFAULT_SUGGESTION_SIZE);

            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                String id = rs.getString("id");
                String title = rs.getString("title");
                Integer year = rs.getInt("year");
                String genre = getGenre(id, connection);
                
                MovieSuggestion suggestion = new MovieSuggestion(id, title, year, genre);
                
                suggestionList.add(suggestion);
            }

            statement.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return suggestionList;
    }
    
    private String getGenre(String movieId, Connection connection) throws SQLException {
        String genreName = "";

        String query = "SELECT genres.name FROM genres INNER JOIN " +
                "( SELECT gim1.genreId FROM genres_in_movies as gim1 " +
                "WHERE gim1.movieId = ? ORDER BY genreId ) as gim2 LIMIT 1;";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, movieId);
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            genreName = rs.getString("name");
        }

        return genreName;
    }
}
