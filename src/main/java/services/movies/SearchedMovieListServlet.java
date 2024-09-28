package services.movies;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

public class SearchedMovieListServlet extends HttpServlet {
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

        String keywords = request.getParameter("keywords");
        String title = request.getParameter("title");
        String director = request.getParameter("director");
        String year = request.getParameter("year");
        String starName = request.getParameter("starName");
        String page = request.getParameter("page");
        String pageSize = request.getParameter("pageSize");

        Gson gson = new Gson();
        JsonObject resultJson = new JsonObject();

        try {
            List<MovieListItem> movies = new ArrayList<>();
            if (keywords != null && !keywords.isEmpty() && !keywords.equals("null")) {
                movies.addAll(services.searchMovies(keywords, page, pageSize, dataSource));
            } else if ((title != null && !title.isBlank() && !title.equals("null")) ||
                    (director != null && !director.isBlank() && !director.equals("null")) ||
                    (year != null && !year.isBlank() && !year.equals("null")) ||
                    (starName != null && !starName.isBlank() && !starName.equals("null"))) {
                movies.addAll(services.searchMovies(title, director, year, starName, page, pageSize, dataSource));
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
