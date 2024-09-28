package services.movies;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import data.movie.MovieListItem;
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

public class BrowsedMovieListServlet extends HttpServlet {

    private DataSource dataSource;

    private MovieListServices services;

    public void init(ServletConfig config) {
        services = new MovieListServices();

        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String genreId = request.getParameter("genreId");
        String startWith = request.getParameter("startWith");
        String page = request.getParameter("page");
        String pageSize = request.getParameter("pageSize");

        Gson gson = new Gson();
        JsonObject resultJson = new JsonObject();

        try {
            List<MovieListItem> movies = new ArrayList<>();
            if (genreId != null && !genreId.isBlank() && !genreId.equals("null")) {
                movies.addAll(services.browseMoviesByGenreId(genreId, page, pageSize, dataSource));
            } else if (startWith != null && !startWith.isBlank() && !startWith.equals("null")) {
                movies.addAll(services.browseMoviesByStartWith(startWith, page, pageSize, dataSource));
            }

            JsonArray moviesJson = gson.toJsonTree(movies).getAsJsonArray();
            resultJson.addProperty("status", "success");
            resultJson.addProperty("count", services.getCount());
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
