package mappers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import data.genre.Genre;
import data.movie.MovieListItem;
import data.star.Star;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static services.movies.MovieListServices.getGenresInMovies;
import static services.movies.MovieListServices.getStarsInMovies;

public class MovieListMapper {

    public static JsonArray mapToList(ResultSet rs) throws SQLException {
        Gson gson = new Gson();

        String currentId = "";

        int i = -1;

        ArrayList<MovieListItem> movieList = new ArrayList<MovieListItem>();

        while (rs.next()) {
            String movieId = rs.getString("id");

            if (!currentId.equals(movieId)) {
                currentId = movieId;

                MovieListItem movieListItem = new MovieListItem(
                        movieId,
                        rs.getString("title"),
                        rs.getString("director"),
                        rs.getInt("year"),
                        rs.getDouble("rating")
                );

                movieList.add(movieListItem);
                i += 1;
            }

            MovieListItem currentItem = movieList.get(i);

            Genre genre = new Genre(rs.getString("genreId"), rs.getString("genreName"));
            if (!currentItem.getGenres().contains(genre) && currentItem.getGenres().size() < 3) {
                currentItem.getGenres().add(genre);
            }

            Star star = new Star(rs.getString("starId"), rs.getString("starName"));
            if (!currentItem.getStars().contains(star) && currentItem.getStars().size() < 3) {
                currentItem.getStars().add(star);
            }
        }

        return gson.toJsonTree(movieList).getAsJsonArray();
    }

    public static List<MovieListItem> mapToArrayList(ResultSet rs, Connection connection) throws SQLException {
        HashMap<String, MovieListItem> movieMap = new HashMap<>();

        while (rs.next()) {

            String id = rs.getString("id");
            String title = rs.getString("title");
            String director = rs.getString("director");
            Integer year = rs.getInt("year");
            Double rating = rs.getDouble("rating");


            MovieListItem movie = new MovieListItem(
                    id, title, director, year, rating,
                    new ArrayList<Genre>(getGenresInMovies(rs.getString("id"), 3, connection)),
                    new ArrayList<Star>(getStarsInMovies(rs.getString("id"), 3, connection))
            );


            if (!movieMap.containsKey(rs.getString("id"))) {
                movieMap.put(id, movie);
            }
        }
        return new ArrayList<>(movieMap.values());
    }
}
