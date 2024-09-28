package mappers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import data.movie.Movie;
import data.star.StarInMovies;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SingleStarMapper {

    public static JsonArray mapToList(ResultSet rs) throws SQLException {
        Gson gson = new Gson();

        ArrayList<StarInMovies> starList = new ArrayList<>();

        boolean isFirst = true;

        while (rs.next()) {
            if (isFirst) {
                StarInMovies star = new StarInMovies(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getInt("birthYear")
                );
                starList.add(star);
                isFirst = false;
            }

            Movie movie = new Movie(
                    rs.getString("movieId"),
                    rs.getString("movieTitle"),
                    rs.getString("movieDirector"),
                    rs.getInt("movieYear")
            );
            if (!starList.get(0).getMovies().contains(movie)) {
                starList.get(0).getMovies().add(movie);
            }
        }

        return gson.toJsonTree(starList).getAsJsonArray();
    }
}
