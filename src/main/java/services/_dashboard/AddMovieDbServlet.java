package services._dashboard;

import com.google.gson.JsonObject;
import data.user.Employee;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

public class AddMovieDbServlet extends HttpServlet {

    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedbMaster");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        Employee employee = (Employee) request.getSession().getAttribute("user");

        if (employee == null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", "failed");
            jsonObject.addProperty("message", "Unauthorized request");
            response.getWriter().write(jsonObject.toString());
            response.setStatus(403);
            return;
        }

        String titleParam = request.getParameter("title");
        String directorParam = request.getParameter("director");
        String genreParam = request.getParameter("genre");
        String yearParam = request.getParameter("year");
        String startNameParam = request.getParameter("starName");
        String birthYearParam = request.getParameter("birthYear");


        try (Connection connection = dataSource.getConnection()) {
            CallableStatement callableSmt= connection.prepareCall("{CALL add_movie(?, ?, ?, ?, ?, ?, ?, ?, ?)}");

            // Set the input parameters for the stored procedure
            callableSmt.setString(1, titleParam);
            callableSmt.setString(2, directorParam);
            callableSmt.setString(4, genreParam);
            callableSmt.setString(5, startNameParam);

            if (yearParam != null && !yearParam.isEmpty() && !yearParam.equals("null")) {
                callableSmt.setInt(3, Integer.parseInt(yearParam)); // Set the parameter normally
            } else {
                throw new Exception("Require year");
            }

            if (birthYearParam != null && !birthYearParam.isEmpty() && !birthYearParam.equals("null")) {
                callableSmt.setInt(6, Integer.parseInt(birthYearParam)); // Set the parameter normally
            } else {
                callableSmt.setNull(6, Types.INTEGER); // Set as NULL
            }

            // Register output parameters
            callableSmt.registerOutParameter(7, Types.VARCHAR); // movieId
            callableSmt.registerOutParameter(8, Types.VARCHAR); // starId
            callableSmt.registerOutParameter(9, Types.INTEGER); // genreId

            // Execute procedure
            callableSmt.execute();

            // Create json result data
            JsonObject jsonResultData = new JsonObject();
            jsonResultData.addProperty("movieId", callableSmt.getString(7));
            jsonResultData.addProperty("starId", callableSmt.getString(8));
            jsonResultData.addProperty("genreId", callableSmt.getString(9));

            // Create json result object
            JsonObject jsonResult = new JsonObject();
            jsonResult.addProperty("status", "success");
            jsonResult.add("data", jsonResultData);

            response.getWriter().write(jsonResult.toString());
            response.setStatus(200);

        } catch (Exception e) {
            // Write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", "failed");
            jsonObject.addProperty("message", e.getMessage());
            response.getWriter().write(jsonObject.toString());

            // Log error to localhost log
            request.getServletContext().log("Error AddMovieDbServlet:", e);
            // Set response status to 500 (Internal Server Error)
            response.setStatus(200);
        }
    }

}
