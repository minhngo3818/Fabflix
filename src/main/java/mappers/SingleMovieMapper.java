package mappers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import data.genre.Genre;
import data.movie.MovieListItem;
import data.star.Star;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SingleMovieMapper {

    public static JsonArray mapToList(ResultSet rs) throws SQLException {
        Gson gson = new Gson();

        boolean isFirst = true;

        ArrayList<MovieListItem> movieList = new ArrayList<MovieListItem>();

        while (rs.next()) {
            if (isFirst) {

                MovieListItem movieListItem = new MovieListItem(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("director"),
                        rs.getInt("year"),
                        rs.getDouble("rating")
                );

                movieList.add(movieListItem);
                isFirst = false;
            }

            MovieListItem currentItem = movieList.get(0);

            Genre genre = new Genre(rs.getString("genreId"), rs.getString("genreName"));
            if (!currentItem.getGenres().contains(genre)) {
                currentItem.getGenres().add(genre);
            }

            Star star = new Star(rs.getString("starId"), rs.getString("starName"));
            if (!currentItem.getStars().contains(star)) {
                currentItem.getStars().add(star);
            }
        }

        return gson.toJsonTree(movieList).getAsJsonArray();
    }
}
