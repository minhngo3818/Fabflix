package services.movies;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import data.movie.MovieSuggestion;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuggestionMovieListServlet extends HttpServlet {
    private DataSource dataSource;

    private MovieSuggestionServices services;

    public void init(ServletConfig config) {
        services = new MovieSuggestionServices();

        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String keywords = request.getParameter("keywords");

        Gson gson = new Gson();
        JsonObject resultJson = new JsonObject();

        try {
            List<MovieSuggestion> movies = new ArrayList<>();
            if (keywords != null && !keywords.isEmpty() && !keywords.equals("null")) {
                movies.addAll(services.searchSuggestedMovies(keywords, dataSource));
            }

            JsonArray moviesJson = gson.toJsonTree(movies).getAsJsonArray();
            resultJson.addProperty("status", "success");
            resultJson.add("data", moviesJson);
            response.setStatus(200);
            response.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
            resultJson.addProperty("status", "fail");
            resultJson.addProperty("message", "Internal server error");
            response.setStatus(500);
            response.getWriter().write(resultJson.toString());
        }
    }
}
