package services.movies;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GenreListServlet extends HttpServlet {
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

        request.getServletContext().log("Getting genres");

        PrintWriter out = response.getWriter();

        try (Connection connection = dataSource.getConnection()) {

            String query = "SELECT genres.id, genres.name from genres ORDER BY genres.name";

            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();

            while (rs.next()) {
                JsonObject genreJson = new JsonObject();
                genreJson.addProperty("id", rs.getString("id"));
                genreJson.addProperty("name", rs.getString("name"));
                jsonArray.add(genreJson);
            }

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
