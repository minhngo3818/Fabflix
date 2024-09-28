package services.movies;

import data.genre.Genre;
import data.movie.MovieListItem;
import data.star.Star;
import mappers.MovieListMapper;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static services.movies.MoviesUtils.formatBooleanKeywords;

public class MovieListServices {
    public static final int DEFAULT_PAGE = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;
    private final List<MovieListItem> movies = new ArrayList<>();
    private int count = 0;  // Total count of results

    public static synchronized Connection getConnection(DataSource ds) throws SQLException{
        return ds.getConnection();
    }

    public List<MovieListItem> searchMovies(String keywords, String page,
                                            String pageSize, DataSource ds) throws Exception {
        // Reset the previous query
        resetResult();
        keywords = formatBooleanKeywords(keywords);

        try (Connection connection = getConnection(ds) ) {

            String query = "SElECT movies.*, ratings.rating FROM movies " +
                    "LEFT JOIN ratings ON ratings.movieId = movies.id " +
                    "WHERE MATCH(title) AGAINST ( ? IN BOOLEAN MODE) " +
                    "ORDER BY title LIMIT ? OFFSET ? ;";

            int limit = getLimit(pageSize);
            int offset = getOffset(page, pageSize);

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, keywords);
            statement.setInt(2, limit);
            statement.setInt(3, offset);

            ResultSet rs = statement.executeQuery();
            movies.addAll(MovieListMapper.mapToArrayList(rs, connection));

            // Count the total results without limit
            List<String> queryVariableList = new ArrayList<>();
            queryVariableList.add(keywords);
            setCount(query, queryVariableList, connection);

            statement.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return movies;
    }

    public List<MovieListItem> searchMovies(String title,String director, String year,
                                            String starName,String page, String pageSize,
                                            DataSource ds) throws Exception {
        // Reset the previous query
        resetResult();
        int limit = getLimit(pageSize);
        int offset = getOffset(page, pageSize);

        try (Connection connection = getConnection(ds) ) {
            String query = "SELECT movies.*, ratings.rating " +
                    "FROM movies LEFT JOIN ratings ON movies.id = ratings.movieId " +
                    "WHERE ";

            // Filter search key types and add condition to query
            List<String> keywordList = new ArrayList<>();

            if (!title.isEmpty()){
                query += "MATCH(title) AGAINST ( ? IN BOOLEAN MODE) AND";
                keywordList.add(formatBooleanKeywords(title));
            }

            if (!director.isEmpty()) {
                query += "MATCH(director) AGAINST ( ? IN BOOLEAN MODE) AND";
                keywordList.add(formatBooleanKeywords(director));
            }

            if (!year.isEmpty()) {
                query += "MATCH(year) AGAINST ( ? IN BOOLEAN MODE) AND";
                keywordList.add((year));
            }

            if (!starName.isEmpty()) {
                query += " movies.id IN (SELECT sim.movieId FROM stars_in_movies AS sim " +
                        "INNER JOIN stars ON stars.id = sim.starId " +
                        "WHERE MATCH(name) AGAINST ( ? IN BOOLEAN MODE)) ";
                keywordList.add(formatBooleanKeywords(starName));
            } else{
                query = query.substring(0, query.length() - 4);
            }
            query += " ORDER BY rating DESC LIMIT ? OFFSET ? ; ";

            System.out.println("Search separate: " + query);
            PreparedStatement statement = connection.prepareStatement(query);
            for (int i = 0; i < keywordList.size(); i++) {
                statement.setString(i + 1, keywordList.get(i));
            }
            statement.setInt(keywordList.size() + 1, limit);
            statement.setInt(keywordList.size() + 2, offset);

            ResultSet rs = statement.executeQuery();
            movies.addAll(MovieListMapper.mapToArrayList(rs, connection));

            // Count the total results without limit
            setCount(query, keywordList, connection);

            statement.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return movies;
    }

    public List<MovieListItem> browseMoviesByGenreId(String genreId, String page, String pageSize,
                                                     DataSource ds) throws Exception {

        // Reset the previous query
        resetResult();

        int limit = getLimit(pageSize);
        int offset = getOffset(page, pageSize);

        try (Connection connection = getConnection(ds) ) {
            String query = "SELECT movies.*, ratings.rating FROM movies " +
                    "INNER JOIN ratings ON ratings.movieId = movies.id " +
                    "WHERE movies.id IN ( SELECT gim.movieId FROM genres_in_movies as gim " +
                    "WHERE gim.genreId = ? ) ORDER BY title LIMIT ? OFFSET ? ;" ;

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, genreId);
            statement.setInt(2, limit);
            statement.setInt(3, offset);

            ResultSet rs = statement.executeQuery();
            movies.addAll(MovieListMapper.mapToArrayList(rs, connection));

            // Count the total results without limit
            List<String> queryVariableList = new ArrayList<>();
            queryVariableList.add(genreId);
            setCount(query, queryVariableList, connection);

            statement.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return movies;
    }

    public List<MovieListItem> browseMoviesByStartWith(String startWith, String page, String pageSize,
                                                       DataSource ds) throws Exception {
        // Reset the previous query
        resetResult();

        int limit = getLimit(pageSize);
        int offset = getOffset(page, pageSize);

        try (Connection connection = getConnection(ds) ) {
            String matchStartWith;
            if (startWith.equals("*")) {
                matchStartWith = "REGEXP ? ";
                startWith = "^[^a-zA-Z0-9].*";
            } else {
                startWith = startWith.toLowerCase() + "%";
                matchStartWith = "LIKE ? ";
            }

            String query = "SELECT movies.*, ratings.rating FROM movies " +
                    "INNER JOIN ratings ON ratings.movieId = movies.id " +
                    "WHERE movies.title " + matchStartWith + " ORDER BY title LIMIT ? OFFSET ? ;" ;

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, startWith);
            statement.setInt(2, limit);
            statement.setInt(3, offset);

            ResultSet rs = statement.executeQuery();
            movies.addAll(MovieListMapper.mapToArrayList(rs, connection));

            // Count the total results without limit
            List<String> queryVariableList = new ArrayList<>();
            queryVariableList.add(startWith);
            setCount(query, queryVariableList, connection);

            statement.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return movies;
    }

    public int getCount() {
        return count;
    }

    private void setCount(String query, List<String> queryVariableList, Connection connection) throws SQLException {
        query = query.replace("ORDER BY title LIMIT ? OFFSET ? ;", "");
        query = "SELECT COUNT(*) AS count FROM ( " + query + " ) as m ;";

        System.out.println(queryVariableList.toString());
        System.out.println(query);

        PreparedStatement statement = connection.prepareStatement(query);
        for (int i = 0; i < queryVariableList.size(); i++) {
            statement.setString(i+1, queryVariableList.get(i));
        }
        ResultSet rs = statement.executeQuery();

        if (rs.next()) {
            count = rs.getInt("count");
        }
    }

    public static List<Genre> getGenresInMovies(String movieId, int limit, Connection connection) throws SQLException {
        // count: use for get top k, if 0 get all
        String query = "SELECT genres.* FROM ( " +
                "SELECT * FROM genres_in_movies WHERE movieId = ? ) gim " +
                "INNER JOIN genres ON id = gim.genreId ORDER BY name ";

        if (limit > 0) query += "LIMIT ? ";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, movieId);

        if (limit > 0) statement.setInt(2, limit);

        ResultSet rs = statement.executeQuery();

        List<Genre> genres = new ArrayList<>();

        while (rs.next()) {
            Genre genre = new Genre(
                    rs.getString("id"),
                    rs.getString("name")
            );
            genres.add(genre);
        }

        statement.close();
        rs.close();
        return genres;
    }

    public static List<Star> getStarsInMovies(String movieId, int limit, Connection connection) throws SQLException {
        // Get top k stars based on the number movies they are in
        String query = "SELECT stars.*, COUNT(sim2.movieId) as movieCount " +
                "FROM (SELECT sim.starId FROM stars_in_movies AS sim WHERE sim.movieId = ? ) AS sim1 " +
                "LEFT JOIN stars_in_movies sim2 ON sim1.starId = sim2.starId " +
                "INNER JOIN stars ON stars.id = sim2.starId " +
                "GROUP BY sim1.starId ORDER BY movieCount DESC ";

        if (limit > 0) query += "LIMIT ? ;";
        else query += " ;";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, movieId);

        if (limit > 0) statement.setInt(2, limit);

        ResultSet rs = statement.executeQuery();

        List<Star> stars = new ArrayList<>();

        while (rs.next()) {
            Star star = new Star(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getInt("birthYear")
            );
            stars.add(star);
        }

        statement.close();
        rs.close();
        return stars;
    }

    private int getOffset(String page, String pageSize) {
        int pageInt = DEFAULT_PAGE; // start at page 1
        int pageSizeInt = DEFAULT_PAGE_SIZE;
        int offsetInt = 0;

        if (page != null && !page.isEmpty() && !page.equals("null"))
            pageInt = Integer.parseInt(page);
        if (pageSize != null && !pageSize.isEmpty() && !pageSize.equals("null"))
            pageSizeInt = Integer.parseInt(pageSize);

        offsetInt = (pageInt - 1) * pageSizeInt;

        return offsetInt;
    }

    private int getLimit(String pageSize) {
        if (pageSize == null || pageSize.equals("null"))
            return DEFAULT_PAGE_SIZE;
        return Integer.parseInt(pageSize);
    }



    private void resetResult() {
        if (!movies.isEmpty() || count > 0) {
            movies.clear();
            count = 0;
        }
    }
}
