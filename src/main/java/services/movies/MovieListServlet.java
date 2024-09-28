package services.movies;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mappers.MovieListMapper;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class MovieListServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String pageSizeParam = request.getParameter("pageSize");

        int pageSize = 20;

        if (pageSizeParam != null && !pageSizeParam.equals("null")) {
            pageSize = Integer.parseInt(pageSizeParam);
        }

        request.getServletContext().log("Getting top " + pageSize + " movies.");

        PrintWriter out = response.getWriter();

        try (Connection connection = dataSource.getConnection()) {

            String query = "SELECT m.*, gm.genreId, gm.genreName, sim.id as starId, sim.name as starName " +
                    "FROM (SELECT movies.*, ratings.rating FROM movies " +
                    "    LEFT JOIN ratings ON ratings.movieiD = movies.id " +
                    "    ORDER BY ratings.rating DESC " +
                    "    LIMIT ? ) AS m " +
                    "LEFT JOIN (SELECT  " +
                    "        genres.id as genreId, " +
                    "        genres.name as genreName, " +
                    "        gim1.movieId " +
                    "    FROM genres_in_movies AS gim1 " +
                    "    JOIN genres ON genres.id = gim1.genreId " +
                    "    WHERE ( " +
                    "        SELECT COUNT(*) " +
                    "        FROM genres_in_movies AS gim2 " +
                    "        WHERE gim1.movieId = gim2.movieId AND gim1.genreId <= gim2.genreId " +
                    "    ) <= 3  " +
                    "    ) AS gm ON gm.movieId = m.id " +
                    "LEFT JOIN (SELECT stars.id, stars.name, sim1.movieId " +
                    "    FROM stars_in_movies AS sim1 " +
                    "    JOIN stars ON stars.id = sim1.starId " +
                    "    WHERE ( " +
                    "        SELECT COUNT(*) " +
                    "        FROM stars_in_movies AS sim2 " +
                    "        WHERE sim1.movieId = sim2.movieId AND sim1.starId <= sim2.starId " +
                    "    ) <= 3 ) AS sim ON sim.movieId = m.id";

            PreparedStatement statement = connection.prepareStatement(query);

            // Set query param pageSize in query statement
            statement.setInt(1, pageSize);

            ResultSet rs = statement.executeQuery();

            // Map the query result to json array
            JsonArray jsonArray = MovieListMapper.mapToList(rs);

            // Count total result query
            query = "SELECT COUNT(*) FROM movies ";

            statement = connection.prepareStatement(query);

            rs = statement.executeQuery();

            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("count", rs.getInt("count"));
            resultJson.add("data", jsonArray);

            rs.close();
            statement.close();
            out.write(resultJson.toString());
            response.setStatus(200);
        } catch (Exception e) {
            // Write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // Log error to localhost log
            request.getServletContext().log("Error MovieListServlet:", e);
            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }
    }
}
