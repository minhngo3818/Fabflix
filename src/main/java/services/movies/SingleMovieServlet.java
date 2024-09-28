package services.movies;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mappers.SingleMovieMapper;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SingleMovieServlet extends HttpServlet {
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

        String id = request.getParameter("id");

        request.getServletContext().log("Getting movie with " + id);

        PrintWriter out = response.getWriter();

        try (Connection connection = dataSource.getConnection()) {

            String query = "SELECT m.*, " +
                    "    ratings.rating, gm.genreId, gm.genreName, " +
                    "    stars.id as starId, stars.name as starName, stars.birthYear as starBirthYear " +
                    "FROM (SELECT * FROM movies WHERE movies.id = ? ) AS m " +
                    "LEFT JOIN ratings ON ratings.movieId = m.id " +
                    "LEFT JOIN stars_in_movies AS sim ON sim.movieId = m.id " +
                    "LEFT JOIN stars ON sim.starId = stars.id " +
                    "JOIN (SELECT " +
                    "     genres.id as genreId, " +
                    "     genres.name as genreName, " +
                    "     gim.movieId " +
                    "     FROM genres_in_movies AS gim " +
                    "     JOIN genres ON genres.id = gim.genreId " +
                    "     ) AS gm ON gm.movieId = m.id " +
                    "ORDER BY stars.name ";

            PreparedStatement statement = connection.prepareStatement(query);

            // Set query param movie id in query statement
            statement.setString(1, id);

            ResultSet rs = statement.executeQuery();

            // Serialize the query result to json array
            JsonArray jsonArray = SingleMovieMapper.mapToList(rs);

            rs.close();
            statement.close();
            out.write(jsonArray.toString());
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
