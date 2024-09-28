package services.sales;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import data.user.Customer;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mappers.SalesMapper;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SalesServlet extends HttpServlet {
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

        Customer customer = (Customer) request.getSession().getAttribute("user");

        PrintWriter out = response.getWriter();

        if (customer == null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", "Failed");
            jsonObject.addProperty("message", "Unauthorized request");
            out.write(jsonObject.toString());
            response.setStatus(403);
        } else {
            try (Connection connection = dataSource.getConnection()) {
                LocalDate today = LocalDate.now();

                // Create a formatter to format the date
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

                // Format the date as a string
                String todayString = today.format(formatter);

                String query = "SELECT sales.*, sales_info.quantity, sales_info.unitPrice, " +
                        "movies.title as movieTitle " +
                        "FROM ( " +
                        "        SELECT customers.id " +
                        "        FROM customers WHERE customers.email = ? ) as c " +
                        "INNER JOIN sales ON sales.customerId = c.id " +
                        "INNER JOIN movies ON movies.id = sales.movieId " +
                        "INNER JOIN sales_info ON sales.id = sales_info.salesId " +
                        "WHERE sales.saleDate = ? ";


                PreparedStatement statement = connection.prepareStatement(query);

                statement.setString(1, customer.getEmail());
                statement.setString(2, todayString);

                ResultSet rs = statement.executeQuery();

                JsonArray jsonArray = SalesMapper.mapToList(rs);

                JsonObject resultJson = new JsonObject();
                resultJson.addProperty("count", jsonArray.size());
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
                request.getServletContext().log("Error SalesServlet:", e);
                // Set response status to 500 (Internal Server Error)
                response.setStatus(500);
            } finally {
                out.close();
            }
        }
    }
}
